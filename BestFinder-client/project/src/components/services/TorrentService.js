import { requestFactory } from './requester';

const upload = `http://localhost:8080/upload`;
const getUrl = `http://localhost:8080/get`;
const baseUrl = 'http://localhost:8080'

export const torrentServiceFactory = () => {
    const request = requestFactory();

    return {
        uploadTorrent: (data, torrent) => request.upload(`${upload}-${torrent}`,data),
        getTorrent: (torrent) => request.get(`${getUrl}-all/${torrent}s`),
        getById: (id, category, data) => request.post(`${getUrl}/${category}/${id}`,data),
        uploadComment: (data, id, category) => request.post(`${upload}/${category}/${id}/comment`,data),
        deleteComment: (torrentId, commentId, category, data) => request.delete(`${baseUrl}/delete/${category}/${torrentId}/comment/${commentId}`, data),
        editCommenet: (torrentId, commentId, category, data) => request.patch(`${baseUrl}/edit/${category}/${torrentId}/comment/${commentId}`, data),
        likeTorrent: (data, category,id) => request.post(`${baseUrl}/${category}/${id}/like`,data),
        unLikeTorrent: (data, category,id) => request.post(`${baseUrl}/${category}/${id}/unlike`,data),
        sortByYear: (category) => request.get(`${baseUrl}/${category}/sort-by-year`),
        sortBySeasons: (category) => request.get(`${baseUrl}/${category}/sort-by-seasons`),
        getCategoryInfo: (category) => request.get(`${baseUrl}/${category}-info`),
    }
};