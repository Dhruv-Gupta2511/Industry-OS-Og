package com.industryos.controller;

import com.industryos.dto.response.ApiResponse;
import com.industryos.entity.AuditLog;
import com.industryos.repository.AuditLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogs(
            @RequestParam(required = false) String actionType) {
        List<AuditLog> logs;
        if (StringUtils.hasText(actionType) && !"ALL".equalsIgnoreCase(actionType)) {
            logs = auditLogRepository.findByActionTypeOrderByCreatedAtDesc(actionType.toUpperCase());
        } else {
            logs = auditLogRepository.findTop100ByOrderByCreatedAtDesc();
        }
        return ResponseEntity.ok(ApiResponse.ok("Live audit log records retrieved", logs));
    }

    private String safeExtract(Map<String, Object> map, String defaultVal, String... keys) {
        if (map == null) return defaultVal;
        for (String k : keys) {
            Object v = map.get(k);
            if (v != null && !v.toString().trim().isEmpty()) {
                return v.toString();
            }
        }
        return defaultVal;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AuditLog>> createAuditLog(
            Authentication authentication,
            @RequestBody(required = false) Map<String, Object> payload) {
        String actionType = safeExtract(payload, "INSERT", "action", "actionType");
        String entityName = safeExtract(payload, "SYSTEM_RECORD", "entity", "entityName");
        String username = authentication != null ? authentication.getName() : safeExtract(payload, "system", "user", "username");
        String auditId = safeExtract(payload, "AUD-" + (System.currentTimeMillis() % 100000), "id", "auditId");

        String summary = safeExtract(payload, "", "payload", "payloadSummary");
        String prev = safeExtract(payload, "", "previousValue", "prevValue");
        String next = safeExtract(payload, "", "newValue", "nextValue");

        AuditLog log = new AuditLog(auditId, actionType.toUpperCase(), entityName.toUpperCase(), username, summary, prev, next);
        log.setCreatedAt(LocalDateTime.now());
        AuditLog saved = auditLogRepository.save(log);

        return ResponseEntity.ok(ApiResponse.ok("Audit event recorded to database", saved));
    }

    @DeleteMapping("/{idOrAuditId}")
    public ResponseEntity<ApiResponse<Void>> deleteAuditLog(@PathVariable String idOrAuditId) {
        try {
            Long numericId = Long.parseLong(idOrAuditId);
            auditLogRepository.deleteById(numericId);
        } catch (NumberFormatException e) {
            auditLogRepository.findByAuditId(idOrAuditId).ifPresent(auditLogRepository::delete);
        }
        return ResponseEntity.ok(ApiResponse.ok("Audit record purged", null));
    }
}

