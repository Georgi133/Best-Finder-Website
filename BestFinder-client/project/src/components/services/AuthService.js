import { requestFactory } from './requester';

const basePartOfUrl = process.env.NODE_ENV === 'development' ?
 'http://localhost:8080' : 'https://lb-spring-app-webfinder.azuremicroservices.io';

const baseUrl = `users`;
const adminUrl = `admins`;

export const authServiceFactory = () => {
    const request = requestFactory();

    return {
        login: (data) => request.post(`${basePartOfUrl}/${baseUrl}/login`, data),
        register: (data) => request.post(`${basePartOfUrl}/${baseUrl}/register`, data),
        // logout: () => request.get(`${baseUrl}/logout`),
        changePassword: (data) => request.patch(`${basePartOfUrl}/${baseUrl}/change-password`, data),
        getUserInfo: (data) => request.post(`${basePartOfUrl}/${baseUrl}/get-user`, data),
        edit: (data) => request.patch(`${basePartOfUrl}/${baseUrl}/edit-profile`, data),
        findUserByEmail: (data) => request.post(`${basePartOfUrl}/${adminUrl}/find-user`,data),
        forgottenPassword: (data) => request.post(`${basePartOfUrl}/${baseUrl}/forgotten/password`,data),
        getUserFullName: () => request.get(`${basePartOfUrl}/${baseUrl}/get`)
    }
};