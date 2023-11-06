import { requestFactory } from './requester';

const adminUrl = `http://localhost:8080/admins`;

export const adminServiceFactory = () => {
    const request = requestFactory();

    return {
        findUserByEmail: (data) => request.post(`${adminUrl}/find-user`,data),
        changeRole: (data) => request.patch(`${adminUrl}/change`,data),
        uploadTorrent: (data) => request.upload(`${adminUrl}/upload-torrent`,data),
        banUser: (data) => request.post(`${adminUrl}/ban-user`,data),
    }
};