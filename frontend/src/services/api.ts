import axios from 'axios';

// Instance Axios utilisée par tout le front (session via cookies)
const api = axios.create({
    baseURL: '/internal',
    headers: {
        'Content-Type': 'application/json'
    },
    withCredentials: true,
});

export default api;

export type SessionLoginResponse = {
    httpHeader: string;
    httpHeaderValue: string;
};

export async function loginSession(
    username: string,
    password: string
): Promise<SessionLoginResponse> {
    const res = await api.post<SessionLoginResponse>('/session', { username, password });
    return res.data;
}

export async function fetchAdminApplications(): Promise<any> {
    const res = await api.get('/applications');
    return res.data;
}

export async function logoutSession(): Promise<void> {
    await api.delete('/session');
}

export function setVipCookies(username: string, sessionValue: string, days: number): void {
    // const cookieNameUser = 'vip-cookie-user';
    // const cookieNameSession = 'vip-cookie-session';
    // const d = new Date();
    // d.setTime(d.getTime() + days * 24 * 60 * 60 * 1000);
    // const expires = 'expires=' + d.toUTCString();
    // document.cookie = `${cookieNameUser}=${encodeURIComponent(username)}; ${expires}; path=/`;
    // document.cookie = `${cookieNameSession}=${encodeURIComponent(sessionValue)}; ${expires}; path=/`;
}

export function clearVipCookies(): void {
    // const past = new Date(0).toUTCString();
    // document.cookie = `vip-cookie-user=; expires=${past}; path=/`;
    // document.cookie = `vip-cookie-session=; expires=${past}; path=/`;
}

// Intercepteur global: si 401/403, on nettoie et on redirige vers login
api.interceptors.response.use(
    (response) => response,
    (error) => {
        const status = error?.response?.status;
        if (status === 401 || status === 403) {
            try { clearVipCookies(); } catch (_) {}
            if (typeof window !== 'undefined') {
                window.location.href = '/new_front/login';
            }
        }
        return Promise.reject(error);
    }
);

export async function fetchApplications() {
    const res = await api.get('/applications');
    return res.data;
}

export async function fetchExecutions() {
    const res = await api.get('/applications');
    return res.data;
}

export async function fetchApplication(id: string) {
    const res = await api.get(`/applications/${id}`);
    return res.data;
}

export async function launchPipeline(parameters: Record<string, string | Record<string, string>>) {
    const name = parameters['name'] as string;
    const pipelineIdentifier = parameters['pipeline'] as string;
    const resultsLocation = '/vip/Home/API/client_tests/';
    const inputValues = parameters['inputValues'] as Record<string, string>;

    const res = await api.post('/executions', {
        name,
        pipelineIdentifier,
        inputValues,
        resultsLocation,
    });
    return res.data;
}