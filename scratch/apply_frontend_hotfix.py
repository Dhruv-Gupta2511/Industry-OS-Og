import os
import re

print("Starting apply_frontend_hotfix.py...")

def update_file(filepath):
    print(f"Updating {filepath}...")
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # 1. Update header nav profile area: add Profile & Settings button and styled Delete Account button
    old_nav_profile = '''                    <button type="button" id="btn-delete-account" class="btn-delete-account" title="Permanently Delete Current Account" style="background:#FEF2F2; color:#991B1B; border:1px solid #FECACA; padding:7px 12px; border-radius:6px; font-size:12px; font-weight:600; cursor:pointer; display:inline-flex; align-items:center; gap:5px; transition:all 0.2s ease; margin-right:6px;">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>
                        <span>Delete Account</span>
                    </button>'''

    new_nav_profile = '''                    <button type="button" id="btn-profile-settings" class="btn-action-outline" title="View Profile & Settings" onclick="openUserProfileModal()" style="padding:6px 11px; font-size:12px; font-weight:600; cursor:pointer; display:inline-flex; align-items:center; gap:5px; margin-right:6px; border-radius:6px;">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="3"></circle><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"></path></svg>
                        <span>Profile</span>
                    </button>
                    <button type="button" id="btn-delete-account" class="btn-delete-account" title="Permanently Delete Current Account" style="background:#FEF2F2; color:#DC2626; border:1.5px solid #DC2626; padding:6px 12px; border-radius:6px; font-size:12px; font-weight:700; cursor:pointer; display:inline-flex; align-items:center; gap:5px; transition:all 0.2s ease; margin-right:6px; box-shadow:0 1px 2px rgba(220,38,38,0.1);">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>
                        <span>Delete Account</span>
                    </button>'''

    if old_nav_profile in content:
        content = content.replace(old_nav_profile, new_nav_profile)
        print("Replaced old_nav_profile.")
    elif "btn-profile-settings" not in content and 'id="btn-delete-account"' in content:
        content = re.sub(r'<button type="button" id="btn-delete-account"[^>]*>[\s\S]*?<span>Delete Account</span>[\s\S]*?</button>', new_nav_profile, content)
        print("Regex replaced btn-delete-account.")

    # 2. Make user-avatar-circle and user-meta-details clickable to open profile
    content = content.replace(
        '<div class="user-avatar-circle" id="user-avatar-initials">AY</div>',
        '<div class="user-avatar-circle" id="user-avatar-initials" style="cursor:pointer;" title="Click to View Profile & Settings" onclick="openUserProfileModal()">AY</div>'
    )
    content = content.replace(
        '<div class="user-meta-details">',
        '<div class="user-meta-details" style="cursor:pointer;" title="Click to View Profile & Settings" onclick="openUserProfileModal()">'
    )

    # 3. Add openUserProfileModal function and enhance account/order deletion logic
    helper_functions = '''
        function normalizeRole(role) {
            if (!role) return 'OPERATOR';
            let r = String(role).toUpperCase().trim();
            if (r === 'CEO' || r === 'HEAD_OF_COMPANY') return 'HEAD_OF_COMPANY';
            if (r === 'RETAILER' || r === 'CUSTOMER') return 'CUSTOMER';
            if (r === 'CONTRACTOR' || r === 'TRANSPORTER') return 'TRANSPORTER';
            if (r === 'DEPT_HEAD') return 'DEPT_HEAD';
            if (r === 'GENERAL_MANAGER' || r === 'GM') return 'GENERAL_MANAGER';
            if (r === 'OPERATOR') return 'OPERATOR';
            return 'EMPLOYEE';
        }

        function openUserProfileModal() {
            const u = AppState.currentUser;
            const fullUser = (AppState.users && AppState.users[u.username]) || {};
            const email = fullUser.email || `${u.username}@industry-os.com`;
            const table = fullUser.table || (u.role === 'CUSTOMER' ? 'customers' : (u.role === 'TRANSPORTER' ? 'contractors' : 'employees'));
            DOM.modalTitle.textContent = "User Profile & Security Settings";
            DOM.modalBody.innerHTML = `
                <div style="font-size:13.5px; color:var(--text-secondary); line-height:1.6; text-align:left;">
                    <div style="display:flex; align-items:center; gap:16px; margin-bottom:16px; padding:14px; background:var(--bg-subtle); border-radius:8px; border:1px solid var(--border-color);">
                        <div style="width:52px; height:52px; border-radius:50%; background:#DC2626; color:#fff; display:flex; align-items:center; justify-content:center; font-size:20px; font-weight:800;">
                            ${DOM.userAvatarInitials ? DOM.userAvatarInitials.textContent : 'US'}
                        </div>
                        <div>
                            <div style="font-size:17px; font-weight:700; color:var(--text-primary);">${u.fullName}</div>
                            <div style="font-size:12px; color:var(--text-muted);">${email} • <span class="role-tag-badge">${u.role}</span></div>
                        </div>
                    </div>
                    <div style="background:var(--bg-card); border:1px solid var(--border-color); border-radius:6px; padding:12px 14px; margin-bottom:16px;">
                        <div style="display:flex; justify-content:space-between; padding:6px 0; border-bottom:1px solid var(--border-color);">
                            <span style="font-weight:600; color:var(--text-muted);">Username / Account ID:</span>
                            <span style="font-family:monospace; font-weight:700; color:var(--text-primary);">${u.username}</span>
                        </div>
                        <div style="display:flex; justify-content:space-between; padding:6px 0; border-bottom:1px solid var(--border-color);">
                            <span style="font-weight:600; color:var(--text-muted);">Assigned Scope / Dept:</span>
                            <span style="color:var(--text-primary);">${u.department}</span>
                        </div>
                        <div style="display:flex; justify-content:space-between; padding:6px 0; border-bottom:1px solid var(--border-color);">
                            <span style="font-weight:600; color:var(--text-muted);">RDBMS Domain Tables:</span>
                            <span style="font-family:monospace; color:#2563EB; font-weight:600;">${table} & user_account (users)</span>
                        </div>
                        <div style="display:flex; justify-content:space-between; padding:6px 0;">
                            <span style="font-weight:600; color:var(--text-muted);">Security Credentials:</span>
                            <span style="color:#10B981; font-weight:600;">Active Bearer JWT Token</span>
                        </div>
                    </div>
                    <div style="background:#FEF2F2; border:1.5px solid #FCA5A5; border-radius:8px; padding:14px; margin-top:14px;">
                        <div style="display:flex; justify-content:space-between; align-items:center;">
                            <div>
                                <strong style="color:#991B1B; font-size:13.5px; display:block;">Danger Zone — Delete Account</strong>
                                <span style="font-size:12px; color:#7F1D1D;">Permanently purges this account from the database and terminates current session.</span>
                            </div>
                            <button type="button" class="btn-action" style="background:#DC2626; color:#FFFFFF; border:none; padding:8px 14px; border-radius:6px; font-size:12px; font-weight:700; cursor:pointer; white-space:nowrap;" onclick="closeModal(); handleAccountDeletionPrompt();">
                                🗑️ Delete Account
                            </button>
                        </div>
                    </div>
                </div>
            `;
            DOM.modalActionBtn.textContent = "Close Settings";
            DOM.modalActionBtn.style.background = "var(--border-color)";
            DOM.modalActionBtn.style.color = "var(--text-primary)";
            DOM.modalActionBtn.onclick = closeModal;
            openModal();
        }
'''

    if "function openUserProfileModal" not in content:
        content = content.replace("/* ACCOUNT DELETION LOGIC */", helper_functions + "\n        /* ACCOUNT DELETION LOGIC */")
        print("Injected helper_functions and openUserProfileModal.")

    # 4. Enhance switchUserRole to ensure normalized persistence and role metadata synchronization
    new_switch_user_role = '''        function switchUserRole(role) {
            AppState.activeUserPerRole = AppState.activeUserPerRole || {};
            const normRole = normalizeRole(role);
            let username = null;

            // 1. Check if an active custom user was saved for this role
            if (AppState.activeUserPerRole[normRole] && AppState.users[AppState.activeUserPerRole[normRole]]) {
                username = AppState.activeUserPerRole[normRole];
            } else if (AppState.activeUserPerRole[role] && AppState.users[AppState.activeUserPerRole[role]]) {
                username = AppState.activeUserPerRole[role];
            } else {
                // Check if any custom registered user matches this normalized role
                for (const [uName, uObj] of Object.entries(AppState.users)) {
                    if (uObj.isCustom && normalizeRole(uObj.role) === normRole) {
                        username = uName;
                        break;
                    }
                }
            }

            // 2. If no custom registered user, fallback to default seed user
            if (!username) {
                if (normRole === 'CUSTOMER') username = 'customer1';
                else if (normRole === 'TRANSPORTER') username = 'transporter1';
                else if (normRole === 'DEPT_HEAD') username = 'depthead_prod';
                else if (normRole === 'HEAD_OF_COMPANY') username = 'ceo';
                else if (normRole === 'GENERAL_MANAGER') username = 'gm';
                else if (normRole === 'EMPLOYEE') username = 'employee1';
                else username = 'operator1';
            }

            if (AppState.users[username]) {
                const u = AppState.users[username];
                AppState.currentUser.username = username;
                AppState.currentUser.fullName = u.fullName;
                AppState.currentUser.role = normRole;
                AppState.currentUser.department = u.department || AppState.currentUser.department;
                AppState.activeUserPerRole[normRole] = username;
                AppState.activeUserPerRole[role] = username;
                saveRegisteredUsersState();
            } else {
                applyRoleMetadata(username, normRole);
            }

            // Keep domain context models in sync with current user so views never revert to old seed strings
            if (normRole === 'CUSTOMER') {
                AppState.customer.info.name = AppState.currentUser.fullName;
                AppState.customer.info.contact = AppState.currentUser.fullName;
            } else if (normRole === 'TRANSPORTER') {
                AppState.transporter.contract.company = AppState.currentUser.fullName;
            }

            AppState.sessionToken = generateToken(username, normRole);
            localStorage.setItem('industry_os_token', AppState.sessionToken);
            DOM.quickRoleSelect.value = normRole;
            updateUserNavDisplay();
            renderDashboard();
            showToast(`Switched active view to ${normRole} (${AppState.currentUser.fullName})`);
        }'''

    # Replace switchUserRole block
    old_switch_pattern = r'function switchUserRole\(role\) \{[\s\S]*?showToast\(`Switched active view to \$\{role\} \(\$\{AppState\.currentUser\.fullName\}\)\`\);\s*\}'
    if re.search(old_switch_pattern, content):
        content = re.sub(old_switch_pattern, new_switch_role_replacement := new_switch_user_role.strip(), content)
        print("Replaced switchUserRole.")

    # 5. Enhance handleSignup to normalize role and keep AppState synchronized
    signup_sync_code = '''            const normRole = normalizeRole(role);
            AppState.users[username] = { fullName, email, role: normRole, department: mappedDomain, password, table: mappedTable, isCustom: true };
            AppState.activeUserPerRole = AppState.activeUserPerRole || {};
            AppState.activeUserPerRole[normRole] = username;
            AppState.activeUserPerRole[role] = username;
            saveRegisteredUsersState();
            AppState.currentUser.username = username;
            AppState.currentUser.fullName = fullName;
            AppState.currentUser.role = normRole;
            AppState.currentUser.department = mappedDomain;

            if (normRole === 'CUSTOMER') {
                AppState.customer.info.name = fullName;
                AppState.customer.info.contact = fullName;
            } else if (normRole === 'TRANSPORTER') {
                AppState.transporter.contract.company = fullName;
            }'''

    old_signup_sync = '''            const activeRole = role === 'CEO' ? 'HEAD_OF_COMPANY' : role;
            AppState.users[username] = { fullName, email, role: activeRole, department: mappedDomain, password, table: mappedTable, isCustom: true };
            AppState.activeUserPerRole = AppState.activeUserPerRole || {};
            AppState.activeUserPerRole[activeRole] = username;
            AppState.activeUserPerRole[role] = username;
            saveRegisteredUsersState();
            AppState.currentUser.username = username;
            AppState.currentUser.fullName = fullName;
            AppState.currentUser.role = activeRole;
            AppState.currentUser.department = mappedDomain;'''

    if old_signup_sync in content:
        content = content.replace(old_signup_sync, signup_sync_code)
        print("Updated handleSignup sync.")

    # 6. Add dedicated Orders & Dispatches table to Customer Services tab with prominent red Delete Order buttons
    customer_services_old = '''            } else if (tab === 'services') {
                DOM.roleCardsGrid.innerHTML = `
                    <div class="dashboard-card">'''

    customer_services_new = '''            } else if (tab === 'services') {
                DOM.roleCardsGrid.innerHTML = `
                    <div class="dashboard-card" style="grid-column: span 3;">
                        <div class="card-header-line">
                            <div class="card-title-group">
                                <div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect><line x1="3" y1="9" x2="21" y2="9"></line><line x1="9" y1="21" x2="9" y2="9"></line></svg></div>
                                <h3>Customer Orders & Services Ledger</h3>
                            </div>
                            <span class="card-badge badge-info">${c.previousOrders.length} Registered Orders</span>
                        </div>
                        <div class="card-body-content" style="overflow-x:auto;">
                            <table class="audit-table" style="width:100%; text-align:left; border-collapse:collapse;">
                                <thead>
                                    <tr style="border-bottom:1px solid var(--border-color); background:var(--bg-subtle);">
                                        <th style="padding:10px 14px;">Sales Order ID</th>
                                        <th style="padding:10px 14px;">Order Date</th>
                                        <th style="padding:10px 14px;">Order Total</th>
                                        <th style="padding:10px 14px;">Fulfillment Status</th>
                                        <th style="padding:10px 14px; text-align:right;">Database Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    ${c.previousOrders.map(order => `
                                        <tr style="border-bottom:1px solid var(--border-color);">
                                            <td style="padding:10px 14px; font-weight:700; font-family:monospace; color:var(--text-primary);">${order.id}</td>
                                            <td style="padding:10px 14px;">${order.date}</td>
                                            <td style="padding:10px 14px; font-weight:700;">₹${Number(order.amount).toLocaleString()}</td>
                                            <td style="padding:10px 14px;"><span class="card-badge ${order.status === 'Delivered' ? 'badge-success' : 'badge-warning'}">${order.status}</span></td>
                                            <td style="padding:10px 14px; text-align:right;">
                                                <button type="button" class="btn-action-outline btn-sm" style="background:#FEE2E2; color:#DC2626; border:1.5px solid #DC2626; padding:5px 12px; font-size:12px; font-weight:700; cursor:pointer; border-radius:4px; box-shadow:0 1px 2px rgba(220,38,38,0.1);" onclick="confirmDeleteOrder('${order.id}')" title="Permanently Delete Sales Order from Database">🗑️ Delete Order</button>
                                            </td>
                                        </tr>
                                    `).join('')}
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="dashboard-card">'''

    if customer_services_old in content:
        content = content.replace(customer_services_old, customer_services_new)
        print("Added Customer Services Orders Ledger table.")

    # 7. Make sure previous orders in Customer Overview has red Delete Order button
    content = content.replace(
        '''<button type="button" class="btn-action-outline btn-sm" style="color:#DC2626; border-color:#FCA5A5; padding:3px 8px; font-size:11px; cursor:pointer;" onclick="confirmDeleteOrder('${order.id}')" title="Permanently Delete Sales Order from Database">Delete</button>''',
        '''<button type="button" class="btn-action-outline btn-sm" style="background:#FEE2E2; color:#DC2626; border:1.5px solid #DC2626; padding:4px 10px; font-size:11.5px; font-weight:700; cursor:pointer; border-radius:4px;" onclick="confirmDeleteOrder('${order.id}')" title="Permanently Delete Sales Order from Database">🗑️ Delete Order</button>'''
    )

    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f"Finished updating {filepath}.")

for p in ["index.html", "frontend/index.html", "industry-os/src/main/resources/static/index.html"]:
    if os.path.exists(p):
        update_file(p)

print("All frontend files updated successfully.")
