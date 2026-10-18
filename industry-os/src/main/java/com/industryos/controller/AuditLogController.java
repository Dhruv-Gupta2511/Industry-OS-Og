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

    @PostMapping
    public ResponseEntity<ApiResponse<AuditLog>> createAuditLog(
            Authentication authentication,
            @RequestBody Map<String, Object> payload) {
        String actionType = payload.getOrDefault("action", payload.getOrDefault("actionType", "INSERT")).toString();
        String entityName = payload.getOrDefault("entity", payload.getOrDefault("entityName", "SYSTEM_RECORD")).toString();
        String username = authentication != null ? authentication.getName() : payload.getOrDefault("user", payload.getOrDefault("username", "system")).toString();
        String auditId = payload.getOrDefault("id", payload.getOrDefault("auditId", "AUD-" + System.currentTimeMillis() % 10000)).toString();

        String summary = payload.getOrDefault("payload", payload.getOrDefault("payloadSummary", "")).toString();
        String prev = payload.getOrDefault("previousValue", payload.getOrDefault("prevValue", "")).toString();
        String next = payload.getOrDefault("newValue", payload.getOrDefault("nextValue", "")).toString();

        AuditLog log = new AuditLog(auditId, actionType.toUpperCase(), entityName.toUpperCase(), username, summary, prev, next);
        log.setCreatedAt(LocalDateTime.now());
        AuditLog saved = auditLogRepository.save(log);

        return ResponseEntity.ok(ApiResponse.ok("Audit event recorded to database", saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAuditLog(@PathVariable Long id) {
        auditLogRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok("Audit record purged", null));
    }
}

