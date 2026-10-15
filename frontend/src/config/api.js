/**
 * INDUSTRY-OS API CONFIGURATION & SERVICE CLIENT
 * Dynamic Render Backend Integration with 5-Second Cold-Sleep Timeout,
 * Fetch Interceptors, JWT Bearer Token Injection, and Graceful Offline Mock Fallbacks.
 */

export const API_CONFIG = {
    // Primary Render Production Endpoint
    PRIMARY_RENDER_URL: 'https://industry-os-backend.onrender.com/api',
    
    // Dynamic Local Development Fallback
    LOCAL_FALLBACK_URL: (typeof process !== 'undefined' && process.env && process.env.REACT_APP_API_URL)
        ? process.env.REACT_APP_API_URL
        : ((typeof window !== 'undefined' && window.location.port === '8080')
            ? 'http://localhost:8080/api'
            : 'http://localhost:5000/api'),

    // Cold-Sleep & Network Timeout (5 Seconds per strict requirement)
    TIMEOUT_MS: 5000,

    // Active Base URL getter & setter with persistent storage
    get BASE_URL() {
        if (typeof localStorage !== 'undefined') {
            const saved = localStorage.getItem('industry_os_api_url');
            if (saved) return saved;
        }
        return this.PRIMARY_RENDER_URL;
    },

    set BASE_URL(newUrl) {
        if (typeof localStorage !== 'undefined') {
            localStorage.setItem('industry_os_api_url', newUrl);
        }
    }
};

/**
 * Global HTTP Fetch Wrapper with Interceptor Headers & Cold-Sleep Fallback
 */
export async function apiRequest(endpoint, options = {}) {
    const url = `${API_CONFIG.BASE_URL}${endpoint.startsWith('/') ? '' : '/'}${endpoint}`;
    
    // Retrieve JWT token dynamically from Storage
    let token = null;
    if (typeof localStorage !== 'undefined') {
        token = localStorage.getItem('industry_os_token') || sessionStorage.getItem('industry_os_token');
    }

    const headers = {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...(options.headers || {})
    };

    // 5-second timeout controller for Render cold start
    const controller = new AbortController();
    const timer = setTimeout(() => controller.abort(), API_CONFIG.TIMEOUT_MS);

    try {
        const response = await fetch(url, {
            ...options,
            headers,
            signal: controller.signal
        });
        clearTimeout(timer);

        if (!response.ok) {
            const errText = await response.text();
            throw new Error(`HTTP ${response.status}: ${errText || response.statusText}`);
        }

        return await response.json();
    } catch (error) {
        clearTimeout(timer);
        const isTimeout = error.name === 'AbortError';
        const isNetwork = error.message.includes('Failed to fetch') || error.message.includes('NetworkError');

        console.warn(`[INDUSTRY-OS API] Request failed for ${endpoint}:`, {
            isTimeout,
            isNetwork,
            message: error.message,
            targetUrl: url
        });

        // Attach error classification for fallback handling
        error.isColdStartTimeout = isTimeout;
        error.isNetworkOrCors = isNetwork;
        throw error;
    }
}

/**
 * Role-Specific API Service Methods with Fallback Mock Providers
 */
export const ApiService = {
    // 1. AUTHENTICATION & SIGN-UP
    auth: {
        async login(credentials, mockFallback) {
            try {
                const res = await apiRequest('/auth/login', {
                    method: 'POST',
                    body: JSON.stringify(credentials)
                });
                return res.data || res;
            } catch (err) {
                console.warn('[API Auth] Fallback to simulated enterprise token:', err.message);
                return mockFallback ? mockFallback() : null;
            }
        },

        async register(userData, mockFallback) {
            try {
                const res = await apiRequest('/auth/register', {
                    method: 'POST',
                    body: JSON.stringify(userData)
                });
                return res.data || res;
            } catch (err) {
                console.warn('[API Auth] Fallback to local user registry:', err.message);
                return mockFallback ? mockFallback() : null;
            }
        }
    },

    // 2. MACHINE OPERATOR DOMAIN
    operator: {
        async getMachines(mockFallback) {
            try {
                const res = await apiRequest('/operator/machines');
                return res.data || res;
            } catch (err) {
                console.warn('[API Operator] Fallback to local machine telemetry:', err.message);
                return mockFallback ? mockFallback() : null;
            }
        },

        async requestSpareParts(partData, mockFallback) {
            try {
                const res = await apiRequest('/operator/spare-parts/request', {
                    method: 'POST',
                    body: JSON.stringify(partData)
                });
                return res.data || res;
            } catch (err) {
                console.warn('[API Operator] Fallback to simulated spare order:', err.message);
                return mockFallback ? mockFallback() : null;
            }
        }
    },

    // 3. CUSTOMER DOMAIN
    customer: {
        async getCart(mockFallback) {
            try {
                const res = await apiRequest('/customer/cart');
                return res.data || res;
            } catch (err) {
                console.warn('[API Customer] Fallback to local cart store:', err.message);
                return mockFallback ? mockFallback() : null;
            }
        },

        async createOrder(orderData, mockFallback) {
            try {
                const res = await apiRequest('/customer/orders', {
                    method: 'POST',
                    body: JSON.stringify(orderData)
                });
                return res.data || res;
            } catch (err) {
                console.warn('[API Customer] Fallback to local order dispatch:', err.message);
                return mockFallback ? mockFallback() : null;
            }
        },

        async getInvoices(mockFallback) {
            try {
                const res = await apiRequest('/customer/invoices');
                return res.data || res;
            } catch (err) {
                console.warn('[API Customer] Fallback to local invoice archive:', err.message);
                return mockFallback ? mockFallback() : null;
            }
        }
    },

    // 4. TRANSPORTER DOMAIN
    transporter: {
        async getFleet(mockFallback) {
            try {
                const res = await apiRequest('/transporter/fleet');
                return res.data || res;
            } catch (err) {
                console.warn('[API Transporter] Fallback to local fleet telemetry:', err.message);
                return mockFallback ? mockFallback() : null;
            }
        },

        async getContracts(mockFallback) {
            try {
                const res = await apiRequest('/transporter/contracts');
                return res.data || res;
            } catch (err) {
                console.warn('[API Transporter] Fallback to local SLA contracts:', err.message);
                return mockFallback ? mockFallback() : null;
            }
        }
    },

    // 5. DEPT HEAD / CEO / EXECUTIVE DOMAIN
    analytics: {
        async getSummary(mockFallback) {
            try {
                const res = await apiRequest('/analytics/summary');
                return res.data || res;
            } catch (err) {
                console.warn('[API Analytics] Fallback to enterprise executive metrics:', err.message);
                return mockFallback ? mockFallback() : null;
            }
        },

        async getEmployees(mockFallback) {
            try {
                const res = await apiRequest('/employees');
                return res.data || res;
            } catch (err) {
                console.warn('[API Analytics] Fallback to employee workstation records:', err.message);
                return mockFallback ? mockFallback() : null;
            }
        }
    }
};

export default ApiService;

