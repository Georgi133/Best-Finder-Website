const requester = async (method, url, data) => {
    const options = {};

    if (method !== 'GET') {
        options.method = method;

        if (data) {
            options.headers = {
                'content-type': 'application/json',
            };

            options.body = JSON.stringify(data);
        }
    }


    const serializedAuth = localStorage.getItem('user');
    if (serializedAuth) {
        const auth = JSON.parse(serializedAuth);
        
        console.log('here')
        if (auth.token) {
            options.headers = {
                ...options.headers,
                "authorization": `Bearer ${auth.token}`
            }
        }

        console.log('here2')
    }

    console.log('requester ' + options.headers)
    const response = await fetch(url, options);
    console.log(response);

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
    }
};
