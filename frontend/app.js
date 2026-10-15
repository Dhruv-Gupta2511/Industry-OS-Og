/**
 * INDUSTRY-OS FRONTEND APPLICATION ENGINE
 * Complete Production-Ready Frontend with Light/Dark Theme, Interactive RBAC,
 * Sign-Up Flow, 4 Functional Navigation Tabs, and Interactive Demo Actions.
 */

// =========================================================================
// APPLICATION STATE & MOCK ENTERPRISE DATABASE
// =========================================================================
const AppState = {
    theme: 'light',
    isAuthenticated: false,
    activeTab: 'overview',
    
    currentUser: {
        username: 'operator1',
        fullName: 'Aarti Yadav',
        role: 'OPERATOR',
        department: 'Production • Shop Floor',
        token: null
    },

    // User Directory (Pre-seeded + Dynamically Registered)
    users: {
        'operator1': { fullName: 'Aarti Yadav', role: 'OPERATOR', department: 'Production • Shop Floor', password: 'password123' },
        'customer1': { fullName: 'Acme Engineering (R. Malhotra)', role: 'CUSTOMER', department: 'Enterprise Client Portal', password: 'password123' },
        'transporter1': { fullName: 'SafeGuard Logistics (Ramesh P.)', role: 'TRANSPORTER', department: 'Logistics Yard • Fleet Hub', password: 'password123' },
        'depthead_prod': { fullName: 'Geeta Gupta', role: 'DEPT_HEAD', department: 'Production Department', password: 'password123' },
        'ceo': { fullName: 'Suresh Pillai', role: 'HEAD_OF_COMPANY', department: 'Executive Suite • Central Office', password: 'password123' },
        'gm': { fullName: 'Rajesh Varma', role: 'GENERAL_MANAGER', department: 'Plant General Management', password: 'password123' },
        'employee1': { fullName: 'Rajesh Sharma', role: 'EMPLOYEE', department: 'Operations & Assembly', password: 'password123' }
    },

    // Operator Domain
    operator: {
        machine: {
            id: 1,
            name: "CNC Lathe",
            model: "Mazak QT-200",
            line: "Assembly Line A",
            plant: "Pune Works",
            status: "Operational",
            healthScore: 94,
            vibration: "1.4 mm/s (Normal)",
            temp: "42°C (Optimal)",
            downtimeHours: 4.5,
            lastService: "2026-09-01",
            recommendation: "Operational tolerances within optimal limits. Spindle bearing vibration normal."
        },
        telemetry: {
            rpm: 2450,
            spindleLoad: "64%",
            coolantPressure: "3.2 Bar",
            runtimeToday: "6.5 hrs",
            totalPartsCut: 184
        },
        spareParts: [
            { id: 1, name: "Bearing 6205", unitCost: 450, stockQty: 40, reorderLevel: 20, used: 4 },
            { id: 2, name: "Hydraulic Seal Kit", unitCost: 2200, stockQty: 12, reorderLevel: 6, used: 2 },
            { id: 3, name: "Coolant Pump 0.5HP", unitCost: 5400, stockQty: 4, reorderLevel: 5, used: 1 },
            { id: 4, name: "Proximity Sensor M18", unitCost: 1250, stockQty: 18, reorderLevel: 8, used: 3 },
            { id: 5, name: "V-Drive Belt B-68", unitCost: 380, stockQty: 25, reorderLevel: 10, used: 2 }
        ],
        maintenanceLogs: [
            { id: "SR-8840", type: "Preventive", date: "2026-10-10", tech: "Ashok Saxena", status: "Resolved", desc: "Spindle bearing check & lubricant flush" },
            { id: "SR-8835", type: "Calibration", date: "2026-09-28", tech: "Ganesh Singh", status: "Resolved", desc: "Tool turret realignment & zero-point set" }
        ]
    },

    // Customer Domain
    customer: {
        info: {
            name: "Acme Engineering Solutions Ltd",
            contact: "R. K. Malhotra",
            city: "Pune, Maharashtra",
            creditLimit: 5000000,
            gstin: "27AAAPL5555C1Z1"
        },
        cartItems: [
            { id: 1, name: "Flange Shaft Assembly", sku: "PRD-AUTO-001", unitPrice: 8500, quantity: 15 },
            { id: 2, name: "Heavy Transmission Gear", sku: "PRD-AUTO-002", unitPrice: 22000, quantity: 4 },
            { id: 3, name: "High-Tensile Hex Bolt M16", sku: "PRD-FST-003", unitPrice: 3200, quantity: 20 }
        ],
        previousOrders: [
            { id: "SO-2026-004", date: "2026-09-20", status: "Delivered", amount: 2615000, items: 230 },
            { id: "SO-2026-003", date: "2026-08-28", status: "In Transit", amount: 1030000, items: 95 },
            { id: "SO-2026-002", date: "2026-08-12", status: "Delivered", amount: 671600, items: 53 }
        ],
        invoice: {
            invoiceId: "INV-2026-0089",
            soId: "SO-2026-004",
            date: "2026-09-21",
            dueDate: "2026-10-21",
            amount: 2615000,
            taxAmount: 470700,
            totalAmount: 3085700,
            status: "Cleared"
        }
    },

    // Transporter Domain
    transporter: {
        contract: {
            company: "SafeGuard Express Logistics & Transport",
            serviceType: "Heavy Freight & Material Haulage",
            plant: "Pune Works Central Hub",
            start: "2025-04-01",
            end: "2027-03-31",
            value: 2400000,
            status: "ACTIVE"
        },
        vehicles: [
            { id: 1, regNo: "MH12AB1234", type: "Truck 10T", capacity: 10.0, driver: "Ramesh Patil", phone: "+91 98220 11111", status: "Available", speed: "0 km/h", location: "Pune Central Yard", trips: 14 },
            { id: 2, regNo: "RJ14CD5678", type: "Truck 16T", capacity: 16.0, driver: "Bhanwar Lal", phone: "+91 98290 22222", status: "On Trip", speed: "64 km/h", location: "Vadodara Highway KM 142", trips: 22 },
            { id: 3, regNo: "TN09EF9012", type: "Container 20ft", capacity: 18.0, driver: "Murugan K", phone: "+91 98400 33333", status: "Available", speed: "0 km/h", location: "Chennai Plant Dock 2", trips: 19 },
            { id: 4, regNo: "MH14GH3456", type: "Mini Truck 3T", capacity: 3.0, driver: "Sandeep More", phone: "+91 98230 44444", status: "On Trip", speed: "52 km/h", location: "Kolkata Corridor", trips: 31 }
        ],
        tripInvoices: [
            { tripId: "TRIP-801", regNo: "RJ14CD5678", destination: "Vadodara Industrial Corridor", cargo: "Flange Shafts", invoiceAmount: 2156000, status: "Paid" },
            { tripId: "TRIP-802", regNo: "MH14GH3456", destination: "Kolkata Central Stores", cargo: "Transmission Gears", invoiceAmount: 1664200, status: "Pending" },
            { tripId: "TRIP-803", regNo: "TN09EF9012", destination: "Coimbatore Hub", cargo: "Fasteners M16", invoiceAmount: 494000, status: "Processing" }
        ]
    },

    // Executive / Dept Head Domain
    executive: {
        metrics: {
            revenue: 8950000,
            invoiced: 8420000,
            collected: 6890000,
            oee: "88.4%",
            uptime: "95.4%",
            headcount: 43,
            machinesTotal: 22,
            machinesOperational: 20
        },
        plants: [
            { id: 1, name: "Pune Works", city: "Pune", capacity: "1,200/day", staff: 22, lines: 3, status: "Optimal" },
            { id: 2, name: "Jaipur Works", city: "Jaipur", capacity: "900/day", staff: 12, lines: 3, status: "Optimal" },
            { id: 3, name: "Chennai Works", city: "Chennai", capacity: "1,000/day", staff: 9, lines: 2, status: "Scheduled Maint" }
        ],
        departments: [
            { id: 1, name: "Production", head: "Geeta Gupta", budget: "₹9.5M", staff: 14, efficiency: "94%" },
            { id: 2, name: "Mechanical Maintenance", head: "Ashok Saxena", budget: "₹4.2M", staff: 8, efficiency: "98%" },
            { id: 3, name: "Quality Assurance", head: "Padma Sharma", budget: "₹3.1M", staff: 5, efficiency: "99.2%" },
            { id: 4, name: "Supply Chain & Logistics", head: "Gaurav Desai", budget: "₹3.3M", staff: 4, efficiency: "91%" }
        ],
        workOrders: [
            { id: "WO-101", product: "Flange Shaft Assembly", line: "Line A", planned: 400, produced: 388, rejects: 12, status: "Completed" },
            { id: "WO-102", product: "Transmission Gear", line: "Line B", planned: 250, produced: 180, rejects: 6, status: "In Progress" },
            { id: "WO-103", product: "Hex Bolt Set M16", line: "Line C", planned: 600, produced: 600, rejects: 8, status: "Completed" }
        ]
    },

    // Employee Domain
    employee: {
        shift: {
            id: "SH-MORNING",
            name: "Morning Shift A",
            timing: "06:00 AM - 02:30 PM",
            workstation: "Workstation #04 - Sub-Assembly",
            supervisor: "Geeta Gupta",
            status: "Checked In"
        },
        hoursWorked: 38.5,
        targetUnits: 150,
        completedUnits: 142,
        safetyStatus: "100% PPE Compliant"
    }
};

// =========================================================================
// DOM ELEMENTS REFERENCE
// =========================================================================
const DOM = {
    // Views
    loginView: document.getElementById('login-view'),
    dashboardView: document.getElementById('dashboard-view'),
    
    // Auth Switcher & Forms
    btnShowLogin: document.getElementById('btn-show-login'),
    btnShowSignup: document.getElementById('btn-show-signup'),
    authSigninSection: document.getElementById('auth-signin-section'),
    authSignupSection: document.getElementById('auth-signup-section'),
    linkGotoSignup: document.getElementById('link-goto-signup'),
    linkGotoSignin: document.getElementById('link-goto-signin'),
    
    loginForm: document.getElementById('login-form'),
    usernameInput: document.getElementById('login-username'),
    passwordInput: document.getElementById('login-password'),
    roleSelect: document.getElementById('login-role'),
    quickRoleChips: document.querySelectorAll('.role-chip'),
    togglePasswordBtn: document.getElementById('toggle-password'),

    signupForm: document.getElementById('signup-form'),
    signupFullname: document.getElementById('signup-fullname'),
    signupUsername: document.getElementById('signup-username'),
    signupPassword: document.getElementById('signup-password'),
    signupRole: document.getElementById('signup-role'),
    signupDept: document.getElementById('signup-dept'),

    // Theme Toggles
    loginThemeToggle: document.getElementById('login-theme-toggle'),
    navThemeToggle: document.getElementById('nav-theme-toggle'),

    // Top Navigation
    quickRoleSelect: document.getElementById('quick-role-select'),
    navUserFullName: document.getElementById('nav-user-fullname'),
    navUserRoleBadge: document.getElementById('nav-user-role-badge'),
    navUserDepartment: document.getElementById('nav-user-department'),
    userAvatarInitials: document.getElementById('user-avatar-initials'),
    logoutBtn: document.getElementById('logout-btn'),

    // Secondary Tabs
    navTabs: document.querySelectorAll('.nav-tab'),

    // Main Dashboard Containers
    contextRoleTitle: document.getElementById('context-role-title'),
    contextRoleSubtitle: document.getElementById('context-role-subtitle'),
    liveClock: document.getElementById('live-clock'),
    refreshDataBtn: document.getElementById('refresh-data-btn'),
    statKpiStrip: document.getElementById('stat-kpi-strip'),
    roleCardsGrid: document.getElementById('role-cards-grid'),

    // Modal
    modal: document.getElementById('detail-modal'),
    modalTitle: document.getElementById('modal-title'),
    modalBody: document.getElementById('modal-body'),
    modalCloseBtn: document.getElementById('modal-close-btn'),
    modalCancelBtn: document.getElementById('modal-cancel-btn'),
    modalActionBtn: document.getElementById('modal-action-btn'),

    // Toast Container
    toastContainer: document.getElementById('toast-container')
};

// =========================================================================
// INITIALIZATION
// =========================================================================
document.addEventListener('DOMContentLoaded', () => {
    initTheme();
    setupEventListeners();
    initClock();
});

// =========================================================================
// THEME MANAGER (LIGHT & DARK MODE WITH PERSISTENCE)
// =========================================================================
function initTheme() {
    const savedTheme = localStorage.getItem('industry_os_theme') || 'light';
    applyTheme(savedTheme, false);
}

function applyTheme(theme, notify = true) {
    AppState.theme = theme;
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('industry_os_theme', theme);

    if (DOM.loginThemeToggle) {
        const label = DOM.loginThemeToggle.querySelector('.theme-label');
        if (label) label.textContent = theme === 'dark' ? 'Dark' : 'Light';
    }

    if (notify) {
        showToast(`Theme switched to ${theme.toUpperCase()} mode.`);
    }
}

function toggleTheme() {
    const newTheme = AppState.theme === 'light' ? 'dark' : 'light';
    applyTheme(newTheme, true);
}

// =========================================================================
// EVENT LISTENERS SETUP
// =========================================================================
function setupEventListeners() {
    // Theme Toggles
    if (DOM.loginThemeToggle) DOM.loginThemeToggle.addEventListener('click', toggleTheme);
    if (DOM.navThemeToggle) DOM.navThemeToggle.addEventListener('click', toggleTheme);

    // Auth Mode Switching (Sign In vs Create Account)
    DOM.btnShowLogin.addEventListener('click', () => switchAuthMode('signin'));
    DOM.btnShowSignup.addEventListener('click', () => switchAuthMode('signup'));
    DOM.linkGotoSignup.addEventListener('click', () => switchAuthMode('signup'));
    DOM.linkGotoSignin.addEventListener('click', () => switchAuthMode('signin'));

    // Quick Role Chips on Login Page
    DOM.quickRoleChips.forEach(chip => {
        chip.addEventListener('click', () => {
            const user = chip.getAttribute('data-user');
            const role = chip.getAttribute('data-role');
            DOM.usernameInput.value = user;
            DOM.roleSelect.value = role;
            showToast(`Preset loaded: ${role} (${user})`);
        });
    });

    // Password Visibility Toggle
    DOM.togglePasswordBtn.addEventListener('click', () => {
        const type = DOM.passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        DOM.passwordInput.setAttribute('type', type);
    });

    // Form Submissions
    DOM.loginForm.addEventListener('submit', handleLogin);
    DOM.signupForm.addEventListener('submit', handleSignup);

    // Logout
    DOM.logoutBtn.addEventListener('click', handleLogout);

    // Quick Role Switcher in Top Navigation
    DOM.quickRoleSelect.addEventListener('change', (e) => {
        switchUserRole(e.target.value);
    });

    // Secondary Navigation Tabs
    DOM.navTabs.forEach(tab => {
        tab.addEventListener('click', () => {
            DOM.navTabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            AppState.activeTab = tab.getAttribute('data-tab');
            renderDashboard();
            showToast(`Switched view to ${tab.querySelector('span').textContent}`);
        });
    });

    // Refresh Data Button
    DOM.refreshDataBtn.addEventListener('click', () => {
        renderDashboard();
        showToast("Live telemetry & status refreshed across all nodes.");
    });

    // Modal Close
    DOM.modalCloseBtn.addEventListener('click', closeModal);
    DOM.modalCancelBtn.addEventListener('click', closeModal);
    DOM.modal.addEventListener('click', (e) => {
        if (e.target === DOM.modal) closeModal();
    });
}

function switchAuthMode(mode) {
    if (mode === 'signup') {
        DOM.btnShowLogin.classList.remove('active');
        DOM.btnShowSignup.classList.add('active');
        DOM.authSigninSection.classList.remove('active');
        DOM.authSignupSection.classList.add('active');
    } else {
        DOM.btnShowSignup.classList.remove('active');
        DOM.btnShowLogin.classList.add('active');
        DOM.authSignupSection.classList.remove('active');
        DOM.authSigninSection.classList.add('active');
    }
}

function initClock() {
    function update() {
        const now = new Date();
        const options = { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true, month: 'short', day: 'numeric', year: 'numeric' };
        DOM.liveClock.textContent = now.toLocaleDateString('en-US', options);
    }
    update();
    setInterval(update, 1000);
}

// =========================================================================
// AUTHENTICATION & SIGNUP FLOW
// =========================================================================
async function handleLogin(e) {
    e.preventDefault();
    const username = DOM.usernameInput.value.trim();
    const password = DOM.passwordInput.value;
    const selectedRole = DOM.roleSelect.value;

    // Check if user exists in local state
    if (AppState.users[username]) {
        const u = AppState.users[username];
        AppState.currentUser.username = username;
        AppState.currentUser.fullName = u.fullName;
        AppState.currentUser.role = selectedRole || u.role;
        AppState.currentUser.department = u.department;
    } else {
        applyRoleMetadata(username, selectedRole);
    }

    // Try backend authentication if online
    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                username: username,
                password: password,
                userType: selectedRole === 'HEAD_OF_COMPANY' ? 'CEO' : selectedRole
            })
        });

        if (response.ok) {
            const json = await response.json();
            if (json.data && json.data.token) {
                AppState.currentUser.token = json.data.token;
            }
        }
    } catch (err) {
        // Backend offline or local demo mode - local state handles seamlessly
    }

    launchAuthenticatedSession();
}

function handleSignup(e) {
    e.preventDefault();
    const fullName = DOM.signupFullname.value.trim();
    const username = DOM.signupUsername.value.trim();
    const password = DOM.signupPassword.value;
    const role = DOM.signupRole.value;
    const department = DOM.signupDept.value;

    if (!fullName || !username || !password) {
        showToast("Please fill in all registration fields.");
        return;
    }

    // Register user in store
    AppState.users[username] = {
        fullName: fullName,
        role: role,
        department: department,
        password: password
    };

    AppState.currentUser.username = username;
    AppState.currentUser.fullName = fullName;
    AppState.currentUser.role = role;
    AppState.currentUser.department = department;

    showToast(`Account created successfully! Welcome, ${fullName}.`);
    launchAuthenticatedSession();
}

function launchAuthenticatedSession() {
    AppState.isAuthenticated = true;
    DOM.loginView.classList.remove('active');
    DOM.dashboardView.classList.add('active');

    DOM.quickRoleSelect.value = AppState.currentUser.role;
    updateUserNavDisplay();
    renderDashboard();
}

function handleLogout() {
    AppState.isAuthenticated = false;
    AppState.currentUser.token = null;
    DOM.dashboardView.classList.remove('active');
    DOM.loginView.classList.add('active');
    switchAuthMode('signin');
    showToast("Signed out from INDUSTRY-OS session.");
}

function switchUserRole(role) {
    let username = 'operator1';
    if (role === 'CUSTOMER') username = 'customer1';
    else if (role === 'TRANSPORTER') username = 'transporter1';
    else if (role === 'DEPT_HEAD') username = 'depthead_prod';
    else if (role === 'HEAD_OF_COMPANY') username = 'ceo';
    else if (role === 'GENERAL_MANAGER') username = 'gm';
    else if (role === 'EMPLOYEE') username = 'employee1';

    if (AppState.users[username]) {
        const u = AppState.users[username];
        AppState.currentUser.username = username;
        AppState.currentUser.fullName = u.fullName;
        AppState.currentUser.role = role;
        AppState.currentUser.department = u.department;
    } else {
        applyRoleMetadata(username, role);
    }

    DOM.quickRoleSelect.value = role;
    updateUserNavDisplay();
    renderDashboard();
    showToast(`Switched active view to ${role}`);
}

function applyRoleMetadata(username, role) {
    AppState.currentUser.role = role;
    AppState.currentUser.username = username;

    switch (role) {
        case 'OPERATOR':
            AppState.currentUser.fullName = "Aarti Yadav";
            AppState.currentUser.department = "Production • Shop Floor";
            break;
        case 'CUSTOMER':
            AppState.currentUser.fullName = "Acme Engineering (R. Malhotra)";
            AppState.currentUser.department = "Enterprise Client Portal";
            break;
        case 'TRANSPORTER':
            AppState.currentUser.fullName = "SafeGuard Logistics (Ramesh P.)";
            AppState.currentUser.department = "Logistics Yard • Fleet Hub";
            break;
        case 'DEPT_HEAD':
            AppState.currentUser.fullName = "Geeta Gupta";
            AppState.currentUser.department = "Production Department";
            break;
        case 'HEAD_OF_COMPANY':
            AppState.currentUser.fullName = "Suresh Pillai";
            AppState.currentUser.department = "Executive Suite • Central Office";
            break;
        case 'GENERAL_MANAGER':
            AppState.currentUser.fullName = "Rajesh Varma";
            AppState.currentUser.department = "Plant General Management";
            break;
        case 'EMPLOYEE':
            AppState.currentUser.fullName = "Rajesh Sharma";
            AppState.currentUser.department = "Operations & Assembly";
            break;
        default:
            AppState.currentUser.fullName = "System Operator";
            AppState.currentUser.department = "Operations";
    }
}

function updateUserNavDisplay() {
    DOM.navUserFullName.textContent = AppState.currentUser.fullName;
    DOM.navUserRoleBadge.textContent = AppState.currentUser.role;
    DOM.navUserDepartment.textContent = AppState.currentUser.department;

    const initials = AppState.currentUser.fullName
        .split(' ')
        .filter(n => n.length > 0)
        .map(n => n[0])
        .join('')
        .substring(0, 2)
        .toUpperCase();
    DOM.userAvatarInitials.textContent = initials;
}

// =========================================================================
// DASHBOARD TAB DISPATCHER & RENDER ENGINE
// =========================================================================
function renderDashboard() {
    const role = AppState.currentUser.role;
    const tab = AppState.activeTab;

    // Render Tab Content per Role
    if (role === 'OPERATOR') {
        renderOperatorView(tab);
    } else if (role === 'CUSTOMER') {
        renderCustomerView(tab);
    } else if (role === 'TRANSPORTER') {
        renderTransporterView(tab);
    } else if (role === 'DEPT_HEAD' || role === 'HEAD_OF_COMPANY' || role === 'GENERAL_MANAGER') {
        renderExecutiveView(tab);
    } else {
        renderEmployeeView(tab);
    }
}

// -------------------------------------------------------------------------
// 1. OPERATOR VIEWS (Overview, Operations, Inventory, Services)
// -------------------------------------------------------------------------
function renderOperatorView(tab) {
    const m = AppState.operator.machine;
    const tel = AppState.operator.telemetry;

    // Context Header
    DOM.contextRoleTitle.textContent = `Operator Console — ${tab.toUpperCase()}`;
    DOM.contextRoleSubtitle.textContent = `Assigned Hardware: ${m.name} (${m.model}) • Plant: ${m.plant} • Line: ${m.line}`;

    // KPI Strip
    DOM.statKpiStrip.innerHTML = `
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Machine Health</span>
                <span class="kpi-value">${m.healthScore}%</span>
                <span class="kpi-subtext">Status: ${m.status}</span>
            </div>
            <div class="kpi-icon-wrap">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2v20M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path></svg>
            </div>
        </div>
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Parts Machined Today</span>
                <span class="kpi-value">${tel.totalPartsCut} units</span>
                <span class="kpi-subtext">Target: 220 units (83.6%)</span>
            </div>
            <div class="kpi-icon-wrap">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline></svg>
            </div>
        </div>
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Spindle Thermal Gradient</span>
                <span class="kpi-value">${m.temp}</span>
                <span class="kpi-subtext">Vibration: ${m.vibration}</span>
            </div>
            <div class="kpi-icon-wrap">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="9"></circle><path d="M12 7v5l3 3"></path></svg>
            </div>
        </div>
    `;

    if (tab === 'overview') {
        DOM.roleCardsGrid.innerHTML = `
            <!-- Card 1: Machine Telemetry & Health -->
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group">
                        <div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="3"></circle><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"></path></svg></div>
                        <h3>Assigned Hardware Health</h3>
                    </div>
                    <span class="card-badge badge-success">${m.status}</span>
                </div>
                <div class="card-body-content">
                    <div class="health-meter-wrap">
                        <div class="meter-header"><span>Overall Efficiency</span><span>${m.healthScore}%</span></div>
                        <div class="meter-bar-track"><div class="meter-bar-fill" style="width: ${m.healthScore}%;"></div></div>
                    </div>
                    <div class="mini-item-row"><span class="mini-item-title">Model</span><span class="mini-item-meta">${m.model}</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Spindle RPM</span><span class="mini-item-meta">${tel.rpm} RPM</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Coolant Pressure</span><span class="mini-item-meta">${tel.coolantPressure}</span></div>
                    <p style="font-size: 12px; color: var(--text-muted);">${m.recommendation}</p>
                </div>
                <div class="card-footer-actions">
                    <button type="button" class="btn-action" onclick="runDiagnostic()">Run Diagnostic</button>
                    <button type="button" class="btn-action-outline" onclick="requestService()">Request Service</button>
                </div>
            </div>

            <!-- Card 2: Live Spare Parts Summary -->
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group">
                        <div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path></svg></div>
                        <h3>Spare Parts Live Stock</h3>
                    </div>
                    <span class="card-badge badge-info">5 SKUs</span>
                </div>
                <div class="card-body-content">
                    ${AppState.operator.spareParts.slice(0, 3).map(part => `
                        <div class="mini-item-row">
                            <div>
                                <div class="mini-item-title">${part.name}</div>
                                <div class="mini-item-meta">Stock: ${part.stockQty} • Reorder: ${part.reorderLevel}</div>
                            </div>
                            <div class="qty-stepper">
                                <button class="qty-btn" onclick="updatePartUsage(${part.id}, -1)">-</button>
                                <span class="qty-val">${part.used}</span>
                                <button class="qty-btn" onclick="updatePartUsage(${part.id}, 1)">+</button>
                            </div>
                        </div>
                    `).join('')}
                </div>
                <div class="card-footer-actions">
                    <button type="button" class="btn-action" onclick="reorderParts(1)">Reorder Parts</button>
                    <button type="button" class="btn-action-outline" onclick="switchTab('inventory')">View All Parts</button>
                </div>
            </div>

            <!-- Card 3: Shift & Work Order Execution -->
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group">
                        <div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg></div>
                        <h3>Shift Work Order Execution</h3>
                    </div>
                    <span class="card-badge badge-warning">Active Run</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Current Job</span><span class="mini-item-meta">Flange Shaft Machining</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Batch Planned</span><span class="mini-item-meta">220 pcs</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Completed Today</span><span class="mini-item-meta">${tel.totalPartsCut} pcs</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Active Hours</span><span class="mini-item-meta">${tel.runtimeToday}</span></div>
                </div>
                <div class="card-footer-actions">
                    <button type="button" class="btn-action" onclick="logShiftHours()">Log Shift Hours</button>
                    <button type="button" class="btn-action-outline" onclick="showToast('Job tolerances locked. ISO-9001 verification pass.')">Verify QC</button>
                </div>
            </div>
        `;
    } else if (tab === 'operations') {
        DOM.roleCardsGrid.innerHTML = `
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg></div><h3>Spindle Telemetry Stream</h3></div>
                    <span class="card-badge badge-success">Live 10Hz</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Rotational Speed</span><span class="mini-item-meta">${tel.rpm} RPM</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Motor Current</span><span class="mini-item-meta">14.8 A</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Spindle Load</span><span class="mini-item-meta">${tel.spindleLoad}</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Bearing Harmonic</span><span class="mini-item-meta">${m.vibration}</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action" onclick="runDiagnostic()">Run Diagnostic</button>
                </div>
            </div>
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path></svg></div><h3>Calibration & Tolerances</h3></div>
                    <span class="card-badge badge-info">ISO-9001</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Axis X Backlash</span><span class="mini-item-meta">0.002 mm (Pass)</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Axis Z Concentricity</span><span class="mini-item-meta">0.001 mm (Pass)</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Tool Turret Index Time</span><span class="mini-item-meta">0.28 sec</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action-outline" onclick="showToast('Zero-point offset calibrated successfully.')">Calibrate Offsets</button>
                </div>
            </div>
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="2" width="20" height="8" rx="2" ry="2"></rect><rect x="2" y="14" width="20" height="8" rx="2" ry="2"></rect></svg></div><h3>Coolant & Lubrication</h3></div>
                    <span class="card-badge badge-success">Normal</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Coolant Flow</span><span class="mini-item-meta">18 L/min</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Oil Sump Level</span><span class="mini-item-meta">88% Capacity</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Filter Pressure Drop</span><span class="mini-item-meta">0.4 Bar</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action-outline" onclick="showToast('Coolant pump cycle initiated.')">Flush Coolant</button>
                </div>
            </div>
        `;
    } else if (tab === 'inventory') {
        DOM.roleCardsGrid.innerHTML = AppState.operator.spareParts.map(part => `
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group">
                        <div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path></svg></div>
                        <h3>${part.name}</h3>
                    </div>
                    <span class="card-badge ${part.stockQty <= part.reorderLevel ? 'badge-danger' : 'badge-success'}">
                        ${part.stockQty <= part.reorderLevel ? 'Low Stock' : 'In Stock'}
                    </span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Unit Cost</span><span class="mini-item-meta">₹${part.unitCost.toLocaleString()}</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Current Stock</span><span class="mini-item-meta"><strong>${part.stockQty}</strong> units</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Reorder Trigger</span><span class="mini-item-meta">${part.reorderLevel} units</span></div>
                    <div class="mini-item-row">
                        <span class="mini-item-title">Quantity Used in Shift</span>
                        <div class="qty-stepper">
                            <button class="qty-btn" onclick="updatePartUsage(${part.id}, -1)">-</button>
                            <span class="qty-val">${part.used}</span>
                            <button class="qty-btn" onclick="updatePartUsage(${part.id}, 1)">+</button>
                        </div>
                    </div>
                </div>
                <div class="card-footer-actions">
                    <button type="button" class="btn-action" onclick="reorderParts(${part.id})">Reorder Parts</button>
                    <button type="button" class="btn-action-outline" onclick="showToast('Part specification datasheet downloaded.')">Datasheet</button>
                </div>
            </div>
        `).join('');
    } else if (tab === 'services') {
        DOM.roleCardsGrid.innerHTML = `
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"></path></svg></div><h3>Maintenance Service Tickets</h3></div>
                    <span class="card-badge badge-info">Mechanical Shop</span>
                </div>
                <div class="card-body-content">
                    ${AppState.operator.maintenanceLogs.map(log => `
                        <div class="mini-item-row">
                            <div>
                                <div class="mini-item-title">${log.id} • ${log.type}</div>
                                <div class="mini-item-meta">${log.desc} (${log.tech})</div>
                            </div>
                            <span class="card-badge badge-success">${log.status}</span>
                        </div>
                    `).join('')}
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action" onclick="requestService()">Request Service</button>
                </div>
            </div>
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 14 14"></polyline></svg></div><h3>Preventive Schedule</h3></div>
                    <span class="card-badge badge-warning">Next: 4 Days</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Next Overhaul</span><span class="mini-item-meta">Oct 19, 2026</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Lubricant Flush</span><span class="mini-item-meta">Mobil Vactra #2</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Lead Tech</span><span class="mini-item-meta">Ashok Saxena</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action-outline" onclick="showToast('Preventive maintenance checklist acknowledged.')">Acknowledge</button>
                </div>
            </div>
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path></svg></div><h3>Tooling Safety Log</h3></div>
                    <span class="card-badge badge-success">Zero Incidents</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Emergency E-Stop</span><span class="mini-item-meta">Tested & Active</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Interlock Shield Door</span><span class="mini-item-meta">Sensor Verified</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">PPE Verification</span><span class="mini-item-meta">Face Guard + Boots (Pass)</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action-outline" onclick="showToast('Safety audit signed off.')">Sign Safety Log</button>
                </div>
            </div>
        `;
    }
}

// -------------------------------------------------------------------------
// 2. CUSTOMER VIEWS (Overview, Operations, Inventory, Services)
// -------------------------------------------------------------------------
function renderCustomerView(tab) {
    const c = AppState.customer;
    const cartTotal = c.cartItems.reduce((acc, item) => acc + (item.unitPrice * item.quantity), 0);
    const taxTotal = Math.round(cartTotal * 0.18);
    const grandTotal = cartTotal + taxTotal;

    DOM.contextRoleTitle.textContent = `Customer Enterprise Portal — ${tab.toUpperCase()}`;
    DOM.contextRoleSubtitle.textContent = `Client: ${c.info.name} • GSTIN: ${c.info.gstin} • Credit Limit: ₹${(c.info.creditLimit/100000).toFixed(1)} Lakhs`;

    DOM.statKpiStrip.innerHTML = `
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Active Cart Value</span>
                <span class="kpi-value">₹${grandTotal.toLocaleString()}</span>
                <span class="kpi-subtext">${c.cartItems.length} items (incl. 18% GST)</span>
            </div>
            <div class="kpi-icon-wrap"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="9" cy="21" r="1"></circle><circle cx="20" cy="21" r="1"></circle><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path></svg></div>
        </div>
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Latest Order Status</span>
                <span class="kpi-value">${c.previousOrders[0].id}</span>
                <span class="kpi-subtext">Status: ${c.previousOrders[0].status}</span>
            </div>
            <div class="kpi-icon-wrap"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="3" width="15" height="13"></rect><polygon points="16 8 20 8 23 11 23 16 16 16 16 8"></polygon><circle cx="5.5" cy="18.5" r="2.5"></circle><circle cx="18.5" cy="18.5" r="2.5"></circle></svg></div>
        </div>
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Available Credit</span>
                <span class="kpi-value">₹${((c.info.creditLimit - grandTotal)/100000).toFixed(2)}L</span>
                <span class="kpi-subtext">Approved Tier 1 Commercial Limit</span>
            </div>
            <div class="kpi-icon-wrap"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="5" width="20" height="14" rx="2"></rect><line x1="2" y1="10" x2="22" y2="10"></line></svg></div>
        </div>
    `;

    if (tab === 'overview' || tab === 'inventory') {
        DOM.roleCardsGrid.innerHTML = `
            <!-- Card 1: Interactive Shopping Cart -->
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group">
                        <div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="9" cy="21" r="1"></circle><circle cx="20" cy="21" r="1"></circle><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path></svg></div>
                        <h3>Active Shopping Cart</h3>
                    </div>
                    <span class="card-badge badge-warning">${c.cartItems.length} Products</span>
                </div>
                <div class="card-body-content">
                    ${c.cartItems.map(item => `
                        <div class="mini-item-row">
                            <div>
                                <div class="mini-item-title">${item.name}</div>
                                <div class="mini-item-meta">${item.sku} • ₹${item.unitPrice.toLocaleString()}/unit</div>
                            </div>
                            <div class="qty-stepper">
                                <button class="qty-btn" onclick="updateCartItemQty(${item.id}, -1)">-</button>
                                <span class="qty-val">${item.quantity}</span>
                                <button class="qty-btn" onclick="updateCartItemQty(${item.id}, 1)">+</button>
                            </div>
                        </div>
                    `).join('')}
                    <div class="invoice-total-row" style="margin-top: 8px;"><span>Subtotal:</span><span>₹${cartTotal.toLocaleString()}</span></div>
                    <div class="invoice-total-row"><span>GST (18%):</span><span>₹${taxTotal.toLocaleString()}</span></div>
                    <div class="invoice-grand-total">Total: ₹${grandTotal.toLocaleString()}</div>
                </div>
                <div class="card-footer-actions">
                    <button type="button" class="btn-action" onclick="checkoutCart()">Checkout Cart</button>
                    <button type="button" class="btn-action-outline" onclick="showToast('Saved to procurement wishlist.')">Save Draft</button>
                </div>
            </div>

            <!-- Card 2: Previous Order History -->
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group">
                        <div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect><line x1="3" y1="9" x2="21" y2="9"></line><line x1="9" y1="21" x2="9" y2="9"></line></svg></div>
                        <h3>Previous Order History</h3>
                    </div>
                    <span class="card-badge badge-success">3 Dispatches</span>
                </div>
                <div class="card-body-content">
                    ${c.previousOrders.map(order => `
                        <div class="mini-item-row">
                            <div>
                                <div class="mini-item-title">${order.id}</div>
                                <div class="mini-item-meta">${order.date} • ₹${order.amount.toLocaleString()}</div>
                            </div>
                            <span class="card-badge ${order.status === 'Delivered' ? 'badge-success' : 'badge-info'}">${order.status}</span>
                        </div>
                    `).join('')}
                </div>
                <div class="card-footer-actions">
                    <button type="button" class="btn-action-outline" onclick="trackFleet(2)">Track Active Delivery</button>
                    <button type="button" class="btn-action" onclick="downloadInvoice('INV-2026-0089')">Download Invoices</button>
                </div>
            </div>

            <!-- Card 3: Dynamic Current Invoice -->
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group">
                        <div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path><polyline points="14 2 14 8 20 8"></polyline><line x1="16" y1="13" x2="8" y2="13"></line><line x1="16" y1="17" x2="8" y2="17"></line><polyline points="10 9 9 9 8 9"></polyline></svg></div>
                        <h3>Current Invoice & Tax Receipt</h3>
                    </div>
                    <span class="card-badge badge-success">${c.invoice.status}</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Invoice #</span><span class="mini-item-meta">${c.invoice.invoiceId}</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Sales Order</span><span class="mini-item-meta">${c.invoice.soId}</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Tax Base (GST 18%)</span><span class="mini-item-meta">₹${c.invoice.taxAmount.toLocaleString()}</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Grand Total</span><span class="mini-item-meta" style="font-weight: 800; color: var(--accent-red);">₹${c.invoice.totalAmount.toLocaleString()}</span></div>
                </div>
                <div class="card-footer-actions">
                    <button type="button" class="btn-action" onclick="downloadInvoice('${c.invoice.invoiceId}')">Download Invoice</button>
                    <button type="button" class="btn-action-outline" onclick="showToast('Payment gateway link generated.')">Pay Online</button>
                </div>
            </div>
        `;
    } else if (tab === 'operations') {
        DOM.roleCardsGrid.innerHTML = `
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="3" width="15" height="13"></rect><polygon points="16 8 20 8 23 11 23 16 16 16 16 8"></polygon><circle cx="5.5" cy="18.5" r="2.5"></circle><circle cx="18.5" cy="18.5" r="2.5"></circle></svg></div><h3>Live Shipment Tracking</h3></div>
                    <span class="card-badge badge-info">En Route</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Truck Reg</span><span class="mini-item-meta">RJ14CD5678 (16T Heavy)</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Route</span><span class="mini-item-meta">Pune Works -> Vadodara Hub</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Live Speed</span><span class="mini-item-meta">64 km/h</span></div>
                    <div class="route-progress-wrap">
                        <div class="route-progress-bar"><div class="route-progress-fill" style="width: 72%;"></div></div>
                        <span style="font-size: 11.5px; color: var(--text-muted);">72% Completed • ETA: 1 hr 20 mins</span>
                    </div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action" onclick="trackFleet(2)">Track Fleet</button>
                </div>
            </div>
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg></div><h3>Quality Certificates (CoA)</h3></div>
                    <span class="card-badge badge-success">Verified</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Tensile Test ISO-6892</span><span class="mini-item-meta">840 MPa (Pass)</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Surface Roughness Ra</span><span class="mini-item-meta">0.8 µm (Optimal)</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Ultrasonic Flaw Check</span><span class="mini-item-meta">Zero Inclusions</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action-outline" onclick="showToast('Certificate of Analysis PDF exported.')">Download CoA</button>
                </div>
            </div>
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2v20M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path></svg></div><h3>Batch Reorder Fast-Track</h3></div>
                    <span class="card-badge badge-warning">Priority</span>
                </div>
                <div class="card-body-content">
                    <p style="font-size: 13px;">Instantly duplicate previous high-volume order with your enterprise discounted rates.</p>
                    <div class="mini-item-row"><span class="mini-item-title">Previous Batch</span><span class="mini-item-meta">SO-2026-004 (230 pcs)</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action" onclick="checkoutCart()">Reorder Previous Batch</button>
                </div>
            </div>
        `;
    } else if (tab === 'services') {
        DOM.roleCardsGrid.innerHTML = `
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path></svg></div><h3>Tax Invoices & GST Returns</h3></div>
                    <span class="card-badge badge-success">GSTR-1 Cleared</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Latest Tax Invoice</span><span class="mini-item-meta">${c.invoice.invoiceId}</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">IRN Status</span><span class="mini-item-meta">Generated via NIC Portal</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">ITC Credit Available</span><span class="mini-item-meta">₹4,70,700</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action" onclick="downloadInvoice('${c.invoice.invoiceId}')">Download Invoice</button>
                </div>
            </div>
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"></path><line x1="12" y1="17" x2="12.01" y2="17"></line></svg></div><h3>Warranty & RMA Claims</h3></div>
                    <span class="card-badge badge-info">36 Months</span>
                </div>
                <div class="card-body-content">
                    <p style="font-size: 13px;">All products backed by comprehensive factory replacement warranties.</p>
                    <div class="mini-item-row"><span class="mini-item-title">Active Claims</span><span class="mini-item-meta">0 Pending</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action-outline" onclick="requestService()">File RMA Request</button>
                </div>
            </div>
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path></svg></div><h3>Key Account Manager</h3></div>
                    <span class="card-badge badge-success">Online</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Manager</span><span class="mini-item-meta">Swati Nair (Commercials)</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Direct Hotline</span><span class="mini-item-meta">+91 20 2712 9901</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action-outline" onclick="showToast('Account Manager alerted. Callback queued.')">Request Callback</button>
                </div>
            </div>
        `;
    }
}

// -------------------------------------------------------------------------
// 3. TRANSPORTER VIEWS (Overview, Operations, Inventory, Services)
// -------------------------------------------------------------------------
function renderTransporterView(tab) {
    const t = AppState.transporter;

    DOM.contextRoleTitle.textContent = `Logistics & Contractor Fleet — ${tab.toUpperCase()}`;
    DOM.contextRoleSubtitle.textContent = `Contractor: ${t.contract.company} • Service: ${t.contract.serviceType} • Value: ₹${(t.contract.value/100000).toFixed(1)} Lakhs`;

    DOM.statKpiStrip.innerHTML = `
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Active Fleet Vehicles</span>
                <span class="kpi-value">${t.vehicles.length} Trucks</span>
                <span class="kpi-subtext">2 On Trip • 2 Ready</span>
            </div>
            <div class="kpi-icon-wrap"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="3" width="15" height="13"></rect><polygon points="16 8 20 8 23 11 23 16 16 16 16 8"></polygon><circle cx="5.5" cy="18.5" r="2.5"></circle><circle cx="18.5" cy="18.5" r="2.5"></circle></svg></div>
        </div>
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Fleet Freight Value</span>
                <span class="kpi-value">₹${t.tripInvoices.reduce((a,b)=>a+b.invoiceAmount,0).toLocaleString()}</span>
                <span class="kpi-subtext">3 Active Trip Invoices</span>
            </div>
            <div class="kpi-icon-wrap"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2v20M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path></svg></div>
        </div>
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Contract Validity</span>
                <span class="kpi-value">${t.contract.status}</span>
                <span class="kpi-subtext">Until ${t.contract.end}</span>
            </div>
            <div class="kpi-icon-wrap"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path></svg></div>
        </div>
    `;

    if (tab === 'overview' || tab === 'operations') {
        DOM.roleCardsGrid.innerHTML = `
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group">
                        <div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="3" width="15" height="13"></rect><polygon points="16 8 20 8 23 11 23 16 16 16 16 8"></polygon><circle cx="5.5" cy="18.5" r="2.5"></circle><circle cx="18.5" cy="18.5" r="2.5"></circle></svg></div>
                        <h3>Fleet Vehicles Assigned</h3>
                    </div>
                    <span class="card-badge badge-info">${t.vehicles.length} Trucks</span>
                </div>
                <div class="card-body-content">
                    ${t.vehicles.map(v => `
                        <div class="mini-item-row">
                            <div>
                                <div class="mini-item-title">${v.regNo} (${v.type})</div>
                                <div class="mini-item-meta">${v.driver} • ${v.phone}</div>
                            </div>
                            <span class="card-badge ${v.status === 'On Trip' ? 'badge-warning' : 'badge-success'}">${v.status}</span>
                        </div>
                    `).join('')}
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action" onclick="trackFleet(2)">Track Fleet</button>
                    <button class="btn-action-outline" onclick="showToast('Driver logs exported.')">Export Drivers</button>
                </div>
            </div>

            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group">
                        <div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="1 6 1 22 8 18 16 22 23 18 23 2 16 6 8 2 1 6"></polygon><line x1="8" y1="2" x2="8" y2="18"></line><line x1="16" y1="6" x2="16" y2="22"></line></svg></div>
                        <h3>Active Dispatch Tracking</h3>
                    </div>
                    <span class="card-badge badge-warning">Live Transit</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Active Unit</span><span class="mini-item-meta">RJ14CD5678 (16T)</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Current Speed</span><span class="mini-item-meta">64 km/h</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Location</span><span class="mini-item-meta">Vadodara Corridor</span></div>
                    <div class="route-progress-wrap">
                        <div class="route-progress-bar"><div class="route-progress-fill" style="width: 72%;"></div></div>
                        <span style="font-size: 11.5px; color: var(--text-muted);">ETA 1 hr 20 mins</span>
                    </div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action" onclick="trackFleet(2)">View GPS Map</button>
                </div>
            </div>

            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group">
                        <div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path></svg></div>
                        <h3>Truck-Specific Trip Invoices</h3>
                    </div>
                    <span class="card-badge badge-success">3 Invoices</span>
                </div>
                <div class="card-body-content">
                    ${t.tripInvoices.map(inv => `
                        <div class="mini-item-row">
                            <div>
                                <div class="mini-item-title">${inv.tripId} • ${inv.regNo}</div>
                                <div class="mini-item-meta">${inv.cargo} • ₹${inv.invoiceAmount.toLocaleString()}</div>
                            </div>
                            <span class="card-badge ${inv.status === 'Paid' ? 'badge-success' : 'badge-warning'}">${inv.status}</span>
                        </div>
                    `).join('')}
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action" onclick="downloadInvoice('TRIP-801')">Download Invoices</button>
                </div>
            </div>
        `;
    } else if (tab === 'inventory') {
        DOM.roleCardsGrid.innerHTML = `
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><circle cx="12" cy="12" r="4"></circle></svg></div><h3>Heavy Fleet Tyres</h3></div>
                    <span class="card-badge badge-success">Stock Good</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Radial 295/80 R22.5</span><span class="mini-item-meta">12 units in yard</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Retread Spares</span><span class="mini-item-meta">6 units in yard</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action" onclick="reorderParts(1)">Reorder Tyres</button>
                </div>
            </div>
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path></svg></div><h3>Vehicle Fluids & DEF</h3></div>
                    <span class="card-badge badge-warning">Refill Alert</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Diesel Exhaust Fluid (DEF)</span><span class="mini-item-meta">180 Liters</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">15W-40 Engine Oil</span><span class="mini-item-meta">45 Liters</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action" onclick="reorderParts(2)">Reorder Fluids</button>
                </div>
            </div>
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path></svg></div><h3>Driver Safety Kits</h3></div>
                    <span class="card-badge badge-success">Assigned</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Reflective Vests</span><span class="mini-item-meta">20 kits</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Ratchet Cargo Straps</span><span class="mini-item-meta">35 sets</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action-outline" onclick="showToast('Cargo lashing audit pass.')">Inspect Gear</button>
                </div>
            </div>
        `;
    } else if (tab === 'services') {
        DOM.roleCardsGrid.innerHTML = `
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path></svg></div><h3>Trip Invoices & Toll Claims</h3></div>
                    <span class="card-badge badge-success">Fastag Sync</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">Total Freight Cleared</span><span class="mini-item-meta">₹21,56,000</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Pending Claims</span><span class="mini-item-meta">₹16,64,200</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action" onclick="downloadInvoice('TRIP-801')">Download Invoices</button>
                </div>
            </div>
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg></div><h3>Contract SLA & Renewal</h3></div>
                    <span class="card-badge badge-info">2027 Expiry</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">On-Time Delivery SLA</span><span class="mini-item-meta">98.2% (Target: 95%)</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Transit Damage Rate</span><span class="mini-item-meta">0.02%</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action-outline" onclick="showToast('SLA performance certificate downloaded.')">Export SLA Report</button>
                </div>
            </div>
            <div class="dashboard-card">
                <div class="card-header-line">
                    <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"></path></svg></div><h3>Fitness & RTO Certifications</h3></div>
                    <span class="card-badge badge-success">Valid</span>
                </div>
                <div class="card-body-content">
                    <div class="mini-item-row"><span class="mini-item-title">National Permits</span><span class="mini-item-meta">All 4 Units Certified</span></div>
                    <div class="mini-item-row"><span class="mini-item-title">Pollution (PUC)</span><span class="mini-item-meta">Valid through 2027</span></div>
                </div>
                <div class="card-footer-actions">
                    <button class="btn-action-outline" onclick="showToast('RTO compliance folder opened.')">View Documents</button>
                </div>
            </div>
        `;
    }
}

// -------------------------------------------------------------------------
// 4. EXECUTIVE / DEPT HEAD VIEWS
// -------------------------------------------------------------------------
function renderExecutiveView(tab) {
    const ex = AppState.executive;
    const isDeptHead = AppState.currentUser.role === 'DEPT_HEAD';

    DOM.contextRoleTitle.textContent = isDeptHead 
        ? `Production Department Head — ${tab.toUpperCase()}`
        : `Executive Enterprise Console — ${tab.toUpperCase()}`;
    DOM.contextRoleSubtitle.textContent = isDeptHead
        ? `Isolated Department Scope: Production & Assembly • 14 Engineers • Line OEE: 94%`
        : `Unrestricted Global Enterprise Access • 3 Connected Works • 22 Machinery Nodes`;

    DOM.statKpiStrip.innerHTML = `
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">${isDeptHead ? 'Department Budget' : 'Total Company Revenue'}</span>
                <span class="kpi-value">${isDeptHead ? '₹9.5M' : '₹8.95M'}</span>
                <span class="kpi-subtext">${isDeptHead ? 'Allocated FY 2026-27' : 'FY 2026-27 Q3 Cleared'}</span>
            </div>
            <div class="kpi-icon-wrap"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2v20M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path></svg></div>
        </div>
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Overall Plant OEE</span>
                <span class="kpi-value">${ex.metrics.oee}</span>
                <span class="kpi-subtext">Uptime: ${ex.metrics.uptime} (Target: 92%)</span>
            </div>
            <div class="kpi-icon-wrap"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 14 14"></polyline></svg></div>
        </div>
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Active Machinery</span>
                <span class="kpi-value">${ex.metrics.machinesOperational} / ${ex.metrics.machinesTotal}</span>
                <span class="kpi-subtext">2 Scheduled Preventive</span>
            </div>
            <div class="kpi-icon-wrap"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="2" width="20" height="8" rx="2" ry="2"></rect><rect x="2" y="14" width="20" height="8" rx="2" ry="2"></rect></svg></div>
        </div>
    `;

    DOM.roleCardsGrid.innerHTML = `
        <div class="dashboard-card">
            <div class="card-header-line">
                <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="12 2 2 7 12 12 22 7 12 2"></polygon><polyline points="2 17 12 22 22 17"></polyline><polyline points="2 12 12 17 22 12"></polyline></svg></div><h3>Plant Network Status</h3></div>
                <span class="card-badge badge-success">3 Works Online</span>
            </div>
            <div class="card-body-content">
                ${ex.plants.map(p => `
                    <div class="mini-item-row">
                        <div>
                            <div class="mini-item-title">${p.name} (${p.city})</div>
                            <div class="mini-item-meta">${p.capacity} • ${p.staff} staff</div>
                        </div>
                        <span class="card-badge ${p.status === 'Optimal' ? 'badge-success' : 'badge-warning'}">${p.status}</span>
                    </div>
                `).join('')}
            </div>
            <div class="card-footer-actions">
                <button class="btn-action" onclick="runDiagnostic()">Run Diagnostic</button>
            </div>
        </div>

        <div class="dashboard-card">
            <div class="card-header-line">
                <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect><line x1="3" y1="9" x2="21" y2="9"></line><line x1="9" y1="21" x2="9" y2="9"></line></svg></div><h3>Active Work Orders</h3></div>
                <span class="card-badge badge-info">3 Running</span>
            </div>
            <div class="card-body-content">
                ${ex.workOrders.map(wo => `
                    <div class="mini-item-row">
                        <div>
                            <div class="mini-item-title">${wo.id} • ${wo.product}</div>
                            <div class="mini-item-meta">${wo.line} • ${wo.produced}/${wo.planned} pcs</div>
                        </div>
                        <span class="card-badge ${wo.status === 'Completed' ? 'badge-success' : 'badge-warning'}">${wo.status}</span>
                    </div>
                `).join('')}
            </div>
            <div class="card-footer-actions">
                <button class="btn-action-outline" onclick="showToast('Work order schedule updated.')">Reschedule</button>
            </div>
        </div>

        <div class="dashboard-card">
            <div class="card-header-line">
                <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path></svg></div><h3>Financials & Invoicing</h3></div>
                <span class="card-badge badge-success">₹8.95M</span>
            </div>
            <div class="card-body-content">
                <div class="mini-item-row"><span class="mini-item-title">Total Invoiced</span><span class="mini-item-meta">₹${ex.metrics.invoiced.toLocaleString()}</span></div>
                <div class="mini-item-row"><span class="mini-item-title">Collections Cleared</span><span class="mini-item-meta">₹${ex.metrics.collected.toLocaleString()}</span></div>
                <div class="mini-item-row"><span class="mini-item-title">Outstanding Receivables</span><span class="mini-item-meta" style="color: var(--accent-red); font-weight: 700;">₹15,30,000</span></div>
            </div>
            <div class="card-footer-actions">
                <button class="btn-action" onclick="downloadInvoice('INV-CORP-2026')">Download Invoices</button>
            </div>
        </div>
    `;
}

// -------------------------------------------------------------------------
// 5. EMPLOYEE VIEWS
// -------------------------------------------------------------------------
function renderEmployeeView(tab) {
    const emp = AppState.employee;

    DOM.contextRoleTitle.textContent = `Plant Staff Workstation — ${tab.toUpperCase()}`;
    DOM.contextRoleSubtitle.textContent = `Assigned Workstation: ${emp.shift.workstation} • Shift: ${emp.shift.name} • Supervisor: ${emp.shift.supervisor}`;

    DOM.statKpiStrip.innerHTML = `
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Shift Attendance</span>
                <span class="kpi-value">${emp.hoursWorked} hrs</span>
                <span class="kpi-subtext">Status: ${emp.shift.status}</span>
            </div>
            <div class="kpi-icon-wrap"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg></div>
        </div>
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Shift Output Units</span>
                <span class="kpi-value">${emp.completedUnits} / ${emp.targetUnits}</span>
                <span class="kpi-subtext">94.6% Completion Rate</span>
            </div>
            <div class="kpi-icon-wrap"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="20 6 9 17 4 12"></polyline></svg></div>
        </div>
        <div class="kpi-card">
            <div class="kpi-info">
                <span class="kpi-title">Safety Compliance</span>
                <span class="kpi-value">100%</span>
                <span class="kpi-subtext">ISO-45001 Verified</span>
            </div>
            <div class="kpi-icon-wrap"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path></svg></div>
        </div>
    `;

    DOM.roleCardsGrid.innerHTML = `
        <div class="dashboard-card">
            <div class="card-header-line">
                <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg></div><h3>Daily Shift Attendance</h3></div>
                <span class="card-badge badge-success">On Duty</span>
            </div>
            <div class="card-body-content">
                <div class="mini-item-row"><span class="mini-item-title">Timing</span><span class="mini-item-meta">${emp.shift.timing}</span></div>
                <div class="mini-item-row"><span class="mini-item-title">Total Hours Logged</span><span class="mini-item-meta">${emp.hoursWorked} hrs</span></div>
                <div class="mini-item-row"><span class="mini-item-title">Biometric Punch</span><span class="mini-item-meta">05:58 AM (Gate 2)</span></div>
            </div>
            <div class="card-footer-actions">
                <button class="btn-action" onclick="logShiftHours()">Log Shift Hours</button>
            </div>
        </div>

        <div class="dashboard-card">
            <div class="card-header-line">
                <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path></svg></div><h3>Tool Crib & PPE Allocation</h3></div>
                <span class="card-badge badge-info">Toolbox #18</span>
            </div>
            <div class="card-body-content">
                <div class="mini-item-row"><span class="mini-item-title">Vernier Caliper Digital</span><span class="mini-item-meta">Checked Out</span></div>
                <div class="mini-item-row"><span class="mini-item-title">Torque Wrench 40Nm</span><span class="mini-item-meta">Checked Out</span></div>
                <div class="mini-item-row"><span class="mini-item-title">Kevlar Cut Gloves</span><span class="mini-item-meta">Allocated</span></div>
            </div>
            <div class="card-footer-actions">
                <button class="btn-action-outline" onclick="showToast('Tool return receipt generated.')">Return Tools</button>
            </div>
        </div>

        <div class="dashboard-card">
            <div class="card-header-line">
                <div class="card-title-group"><div class="line-art-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path></svg></div><h3>Payroll & Monthly Payslip</h3></div>
                <span class="card-badge badge-success">Deposited</span>
            </div>
            <div class="card-body-content">
                <div class="mini-item-row"><span class="mini-item-title">Month</span><span class="mini-item-meta">September 2026</span></div>
                <div class="mini-item-row"><span class="mini-item-title">Gross Salary</span><span class="mini-item-meta">₹42,500</span></div>
                <div class="mini-item-row"><span class="mini-item-title">Overtime Incentive</span><span class="mini-item-meta">₹4,800</span></div>
            </div>
            <div class="card-footer-actions">
                <button class="btn-action" onclick="downloadInvoice('PAYSLIP-2026-09')">Download Invoices</button>
            </div>
        </div>
    `;
}

// =========================================================================
// INTERACTIVE ACTION HANDLERS WITH ASYNC DEMO SIMULATIONS
// =========================================================================

// Helper for switching tabs programmatically
function switchTab(tabKey) {
    DOM.navTabs.forEach(tab => {
        if (tab.getAttribute('data-tab') === tabKey) {
            tab.classList.add('active');
        } else {
            tab.classList.remove('active');
        }
    });
    AppState.activeTab = tabKey;
    renderDashboard();
}

// 1. Run Diagnostic
function runDiagnostic() {
    showToast("Running machine harmonic vibration & thermal diagnostic...");
    
    setTimeout(() => {
        AppState.operator.machine.healthScore = 99;
        AppState.operator.machine.status = "Optimal";
        AppState.operator.machine.vibration = "1.1 mm/s (Peak Precision)";
        AppState.operator.machine.temp = "39.5°C (Ideal)";
        renderDashboard();
        showToast("Diagnostic Complete: Machine Mazak QT-200 calibrated. All harmonic telemetry normal.");
    }, 1200);
}

// 2. Request Service
function requestService() {
    const ticketId = `SR-${Math.floor(1000 + Math.random() * 9000)}`;
    const newLog = {
        id: ticketId,
        type: "Corrective Maint",
        date: new Date().toISOString().split('T')[0],
        tech: "Ashok Saxena",
        status: "Dispatched",
        desc: "Automated alert: High-precision tolerance inspection requested"
    };

    AppState.operator.maintenanceLogs.unshift(newLog);
    renderDashboard();
    showToast(`Service Request #${ticketId} dispatched to Mechanical Maintenance Team!`);
}

// 3. Reorder Parts
function reorderParts(partId) {
    const part = AppState.operator.spareParts.find(p => p.id === partId) || AppState.operator.spareParts[0];
    part.stockQty += 10;
    const poNum = `PO-2026-${Math.floor(1000 + Math.random() * 9000)}`;
    renderDashboard();
    showToast(`Reorder Confirmed! Purchase Order #${poNum} created for 10x ${part.name}.`);
}

// 4. Update Part Usage
function updatePartUsage(partId, delta) {
    const part = AppState.operator.spareParts.find(p => p.id === partId);
    if (!part) return;
    if (part.used + delta < 0) return;
    if (part.stockQty - delta < 0) {
        showToast(`Cannot use more ${part.name}. Stock is depleted.`);
        return;
    }

    part.used += delta;
    part.stockQty -= delta;
    renderDashboard();
    showToast(`Updated ${part.name}: Used = ${part.used}, Stock Remaining = ${part.stockQty}`);
}

// 5. Update Cart Item Qty
function updateCartItemQty(itemId, delta) {
    const item = AppState.customer.cartItems.find(i => i.id === itemId);
    if (!item) return;
    if (item.quantity + delta < 1) {
        showToast("Minimum order quantity is 1 unit.");
        return;
    }

    item.quantity += delta;
    renderDashboard();
    showToast(`Updated ${item.name} quantity to ${item.quantity}.`);
}

// 6. Checkout Cart
function checkoutCart() {
    const c = AppState.customer;
    const cartTotal = c.cartItems.reduce((acc, item) => acc + (item.unitPrice * item.quantity), 0);
    const taxTotal = Math.round(cartTotal * 0.18);
    const grandTotal = cartTotal + taxTotal;
    const soId = `SO-2026-${Math.floor(1000 + Math.random() * 9000)}`;
    const invId = `INV-2026-${Math.floor(1000 + Math.random() * 9000)}`;

    const newOrder = {
        id: soId,
        date: new Date().toISOString().split('T')[0],
        status: "In Transit",
        amount: grandTotal,
        items: c.cartItems.reduce((a,b)=>a+b.quantity, 0)
    };

    c.previousOrders.unshift(newOrder);
    c.invoice = {
        invoiceId: invId,
        soId: soId,
        date: new Date().toISOString().split('T')[0],
        dueDate: new Date(Date.now() + 30*24*60*60*1000).toISOString().split('T')[0],
        amount: cartTotal,
        taxAmount: taxTotal,
        totalAmount: grandTotal,
        status: "Cleared"
    };

    renderDashboard();
    showToast(`Order #${soId} Confirmed! Dynamic Tax Invoice generated.`);
    downloadInvoice(invId);
}

// 7. Download / View Invoice
function downloadInvoice(invoiceId) {
    DOM.modalTitle.textContent = `Tax Invoice Inspection — ${invoiceId}`;
    
    const items = AppState.customer.cartItems;
    const subtotal = items.reduce((acc, item) => acc + (item.unitPrice * item.quantity), 0);
    const cgst = Math.round(subtotal * 0.09);
    const sgst = Math.round(subtotal * 0.09);
    const grandTotal = subtotal + cgst + sgst;

    DOM.modalBody.innerHTML = `
        <div style="font-family: inherit;">
            <div style="display: flex; justify-content: space-between; border-bottom: 2px solid var(--accent-red); padding-bottom: 12px; margin-bottom: 16px;">
                <div>
                    <h2 style="font-size: 18px; font-weight: 800; color: var(--text-primary);">INDUSTRY-OS MANUFACTURING LTD</h2>
                    <p style="font-size: 12px; color: var(--text-muted);">Plot 42, Hinjewadi Phase II, Pune Works, MH 411057</p>
                    <p style="font-size: 12px; color: var(--text-muted);">GSTIN: 27AABCI9021L1Z8 • Tax Invoice (Form GST INV-1)</p>
                </div>
                <div style="text-align: right;">
                    <span class="card-badge badge-success" style="font-size: 12px;">TAX INVOICE</span>
                    <p style="font-size: 13px; font-weight: 700; margin-top: 6px; color: var(--text-primary);">${invoiceId}</p>
                    <p style="font-size: 12px; color: var(--text-muted);">Date: ${new Date().toLocaleDateString()}</p>
                </div>
            </div>

            <div style="display: flex; justify-content: space-between; margin-bottom: 16px; font-size: 12.5px;">
                <div>
                    <span style="font-weight: 700; color: var(--text-primary);">Billed To:</span>
                    <p style="color: var(--text-secondary);">${AppState.customer.info.name}</p>
                    <p style="color: var(--text-muted);">${AppState.customer.info.city} • GSTIN: ${AppState.customer.info.gstin}</p>
                </div>
                <div style="text-align: right;">
                    <span style="font-weight: 700; color: var(--text-primary);">Payment Terms:</span>
                    <p style="color: var(--text-secondary);">Net 30 Days Commercial Credit</p>
                    <p style="color: var(--text-muted);">Mode: NEFT / RTGS</p>
                </div>
            </div>

            <table class="invoice-table">
                <thead>
                    <tr>
                        <th>Item Description</th>
                        <th>HSN Code</th>
                        <th>Qty</th>
                        <th>Rate (₹)</th>
                        <th>Amount (₹)</th>
                    </tr>
                </thead>
                <tbody>
                    ${items.map(i => `
                        <tr>
                            <td><strong>${i.name}</strong><br><span style="font-size: 11px; color: var(--text-muted);">${i.sku}</span></td>
                            <td>8483</td>
                            <td>${i.quantity}</td>
                            <td>₹${i.unitPrice.toLocaleString()}</td>
                            <td>₹${(i.unitPrice * i.quantity).toLocaleString()}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>

            <div class="invoice-total-box">
                <div class="invoice-total-row"><span>Subtotal (Excl. Tax):</span><span>₹${subtotal.toLocaleString()}</span></div>
                <div class="invoice-total-row"><span>Central GST (CGST @ 9%):</span><span>₹${cgst.toLocaleString()}</span></div>
                <div class="invoice-total-row"><span>State GST (SGST @ 9%):</span><span>₹${sgst.toLocaleString()}</span></div>
                <div class="invoice-grand-total invoice-total-row"><span>Grand Total (INR):</span><span>₹${grandTotal.toLocaleString()}</span></div>
            </div>
        </div>
    `;

    DOM.modalActionBtn.textContent = "Print / Export PDF";
    DOM.modalActionBtn.onclick = () => {
        showToast("Generating official printable PDF with digital stamp...");
        window.print();
    };

    openModal();
}

// 8. Track Fleet
function trackFleet(vehicleId) {
    const v = AppState.transporter.vehicles.find(item => item.id === vehicleId) || AppState.transporter.vehicles[1];
    DOM.modalTitle.textContent = `Fleet GPS Telemetry — Truck ${v.regNo}`;

    DOM.modalBody.innerHTML = `
        <div style="font-family: inherit;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px;">
                <div>
                    <h3 style="font-size: 16px; font-weight: 800; color: var(--text-primary);">${v.type} (${v.regNo})</h3>
                    <p style="font-size: 12.5px; color: var(--text-muted);">Assigned Driver: ${v.driver} • ${v.phone}</p>
                </div>
                <span class="card-badge badge-warning" style="font-size: 12px;">Active Transit</span>
            </div>

            <div style="background: var(--bg-subtle); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 18px; margin-bottom: 16px;">
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px; font-size: 13px;">
                    <div><span style="color: var(--text-muted);">Current Speed:</span> <strong>${v.speed}</strong></div>
                    <div><span style="color: var(--text-muted);">Odometer:</span> <strong>142,850 KM</strong></div>
                    <div><span style="color: var(--text-muted);">Route Origin:</span> <strong>Pune Works Central Dock</strong></div>
                    <div><span style="color: var(--text-muted);">Destination:</span> <strong>Vadodara Logistics Hub</strong></div>
                    <div><span style="color: var(--text-muted);">GPS Coordinates:</span> <strong>18.5204° N, 73.8567° E</strong></div>
                    <div><span style="color: var(--text-muted);">Payload Weight:</span> <strong>14.2 Tons / 16.0T</strong></div>
                </div>

                <div class="route-progress-wrap" style="margin-top: 16px;">
                    <div class="meter-header"><span>Route Completion</span><span>72%</span></div>
                    <div class="route-progress-bar"><div class="route-progress-fill" style="width: 72%;"></div></div>
                    <p style="font-size: 11.5px; color: var(--text-muted); margin-top: 4px;">ETA: 1 hr 20 mins • Weather: Clear • Highway NH48 Traffic: Smooth</p>
                </div>
            </div>
        </div>
    `;

    DOM.modalActionBtn.textContent = "Share Live GPS Link";
    DOM.modalActionBtn.onclick = () => {
        showToast("Live GPS link copied to clipboard: https://industry-os.local/fleet/gps/RJ14CD5678");
        closeModal();
    };

    openModal();
}

// 9. Log Shift Hours
function logShiftHours() {
    AppState.employee.hoursWorked += 8.0;
    renderDashboard();
    showToast("8.0 Shift Hours successfully logged to biometric enterprise timesheet!");
}

// =========================================================================
// MODAL & TOAST UTILITIES
// =========================================================================
function openModal() {
    DOM.modal.classList.add('active');
}

function closeModal() {
    DOM.modal.classList.remove('active');
}

function showToast(message) {
    if (!DOM.toastContainer) return;
    
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.textContent = message;

    DOM.toastContainer.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('toast-fadeout');
        setTimeout(() => toast.remove(), 350);
    }, 3200);
}
