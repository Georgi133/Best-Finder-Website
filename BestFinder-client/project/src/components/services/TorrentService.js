import { requestFactory } from './requester';


const upload = `upload`;
const getUrl = `get`;
const baseUrl = process.env.NODE_ENV  === 'development' ? 
'http://localhost:8080' : 'https://lb-spring-app-webfinder.azuremicroservices.io';

// http://localhost:8080

export const torrentServiceFactory = () => {
    const request = requestFactory();

    return {
        uploadTorrent: (data, torrent) => request.upload(`${baseUrl}/${upload}-${torrent}`,data),
        getTorrent: (torrent) => request.get(`${baseUrl}/${getUrl}-all/${torrent}`),
        getById: (id, category) => request.get(`${baseUrl}/${getUrl}/${category}/${id}`),
        uploadComment: (data, id, category) => request.post(`${baseUrl}/${upload}/${category}/${id}/comment`,data),
        deleteComment: (torrentId, commentId, category) => request.delete(`${baseUrl}/delete/${category}/${torrentId}/comment/${commentId}`),
        editCommenet: (torrentId, commentId, category, data) => request.patch(`${baseUrl}/edit/${category}/${torrentId}/comment/${commentId}`, data),
        likeTorrent: (category,id) => request.post(`${baseUrl}/${category}/${id}/like`),
        unLikeTorrent: (category,id) => request.post(`${baseUrl}/${category}/${id}/unlike`),
        sortByYear: (category) => request.get(`${baseUrl}/${category}/sort-by-year`),
        sortBySeasons: (category) => request.get(`${baseUrl}/${category}/sort-by-seasons`),
        getCategoryInfo: (category) => request.get(`${baseUrl}/${category}-info`, '', 'dont take ip on home page'),
        sortAndSearchByYear: (category,data) => request.post(`${baseUrl}/${getUrl}-all/${category}/filtered-by-year`, data),
        sortAndSearchByLikes:(category,data) => request.post(`${baseUrl}/${getUrl}-all/${category}/filtered-by-likes`, data),
    }
};