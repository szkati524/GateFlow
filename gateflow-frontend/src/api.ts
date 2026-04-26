export const apiFetch = (url: string, options: RequestInit = {}) => {
    const token = localStorage.getItem("token");
    
    
    const headers: Record<string, string> = {
        "Content-Type": "application/json",
        ...(options.headers as Record<string, string>),
    };

    if (token) {
        headers["Authorization"] = `Bearer ${token}`;
    }
    
    return fetch(url, { ...options, headers });
};