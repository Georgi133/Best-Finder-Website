import { requestFactory } from './requester';

const adminUrl = `admins`;
const baseUrl = process.env.NODE_ENV  === 'development' ? 
'http://localhost:8080' : 'https://lb-spring-app-webfinder.azuremicroservices.io';

const url = `${baseUrl}/${adminUrl}`

export const adminServiceFactory = () => {
    const request = requestFactory();

    return {
        findUserByEmail: (data) => request.post(`${url}/find-user`,data),
        changeRole: (data) => request.patch(`${url}/change`,data),
        uploadTorrent: (data) => request.upload(`${url}/upload-torrent`,data),
        banUser: (data) => request.post(`${url}/ban-user`,data),
    }
};