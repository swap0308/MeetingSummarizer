export async function serviceRequest(url: string, payload?: any) {
    const configObj = payload ? { method: POST, body: JSON.stringify(payload) } : undefined;
    return fetch(url, configObj)
        .then(res => res.json());
};

export const POST = "POST";
export const baseUrl = "https://dogapi.dog/api/v2";