
const requester = async (method, url, data) => {
    const options = {};

    // if (method !== 'GET') {
        options.method = method;

        console.log(data);
        if (data) {
            options.headers = {
                'content-type': 'application/json',
            };
            options.body = JSON.stringify(data);
        }
    // }


    const token = localStorage.getItem('token');
    if (token) {
        const auth = JSON.parse(token); // is with ""
        //when parse removes them
        
        if (auth) {
            options.headers = {
                ...options.headers,
                "authorization": `Bearer ${auth}`
            }
        }

    }

    const response = await fetch(url, options);

    const result = await response.json();
    

    // if (!response.ok) {
    //     throw result;
    // }

    return result;
};

const uploadImage = async (method, url, formData) => {


    const token = localStorage.getItem('token');
        const auth = JSON.parse(token); // is with ""
        //when parse removes them

    const response = await fetch(url, 
        {
        method: method,
        headers: {
            "authorization": `Bearer ${auth}`
        },
        body: formData
    })

    const result = await response.json();
    

    // if (!response.ok) {
    //     throw result;
    // }

    return result;
};


export const requestFactory = () => {
    return {
        get: requester.bind(null, 'GET'),
        post: requester.bind(null, 'POST'),
        put: requester.bind(null, 'PUT'),
        patch: requester.bind(null, 'PATCH'),
        delete: requester.bind(null, 'DELETE'),
        upload: uploadImage.bind(null, 'POST'),
    }
};
