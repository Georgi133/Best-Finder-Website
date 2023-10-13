import { requestFactory } from './requester';

const baseUrl = `http://localhost:8080`;

export const authServiceFactory = () => {
    const request = requestFactory();

    return {
        login: (data) => request.post(`${baseUrl}/login`, data),
        register: (data) => request.post(`${baseUrl}/register`, data),
        logout: () => request.get(`${baseUrl}/logout`),
        message:() => request.get(`${baseUrl}/message222`),
    }
};