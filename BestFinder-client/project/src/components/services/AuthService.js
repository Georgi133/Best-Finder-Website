import { requestFactory } from './requester';

const baseUrl = `http://localhost:8080/users`;
const adminUrl = `http://localhost:8080/admin`;

export const authServiceFactory = () => {
    const request = requestFactory();

    return {
        login: (data) => request.post(`${baseUrl}/login`, data),
        register: (data) => request.post(`${baseUrl}/register`, data),
        // logout: () => request.get(`${baseUrl}/logout`),
        changePassword: (data) => request.patch(`${baseUrl}/change-password`, data),
        getUserInfo: (data) => request.post(`${baseUrl}/get-user`, data),
        edit: (data) => request.patch(`${baseUrl}/edit-profile`, data),
        findUserByEmail: (data) => request.post(`${adminUrl}/find-user`,data),
    

    }
};