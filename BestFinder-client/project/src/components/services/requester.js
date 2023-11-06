
const requester = async (method, url, data) => {
  let ipUser = '';

  await fetch('https://api.ipify.org')
           .then((res) => res.text())
            .then(ip => ipUser = ip)
           .catch(err => console.log('error when getting ip ' + err));
                           
  const options = {};

  // if (method !== 'GET') {
  options.method = method;

  const language = localStorage.getItem("lang");
  const lang = JSON.parse(language);

  if (data) {
    options.headers = {
      "content-type": "application/json",
    };
    options.body = JSON.stringify(data);
  };

  if(lang) {
    options.headers = {
      ...options.headers,
      "Accept-Language": lang.lang,
    }
  };

  if(ipUser) {
    options.headers = {
      ...options.headers,
      "X-Forwarded-For": ipUser,
    }
  }

  const token = localStorage.getItem("token");
  if (token) {
    const auth = JSON.parse(token); // is with ""
    //when parse removes them

    if (auth) {
      options.headers = {
        ...options.headers,
        authorization: `Bearer ${auth}`,
      };
    }
  }


  try {
    const response = await fetch(url, options);
    

    if (response.ok || response.status === 201) {
     
      return await response.json();
    } else {
      if (response.status === 403) {
        throw new Error("forbidden");
      }
      const result = await response.json();
      throw new Error(
        Object.entries(result).map(([key, value]) => key + ":" + value + "!") +
          " " +
          response.status
      );
    }
  } catch (error) {
    throw new Error(error.message);
  }
};

const uploadTorrent = async (method, url, formData) => {
  const token = localStorage.getItem("token");
  const auth = JSON.parse(token); // is with ""
  //when parse removes them

  let ipUser = '';

  await fetch('https://api.ipify.org')
           .then((res) => res.text())
            .then(ip => ipUser = ip)
           .catch(console.log(''));

  try {
    const response = await fetch(url, {
      method: method,
      headers: {
        authorization: `Bearer ${auth}`,
        "X-Forwarded-For": ipUser,
      },
      body: formData,
    });

    if (response.ok || response.status === 201) {
      return await response.json();
    } else {
      if (response.status === 403) {
        throw new Error("forbidden");
      }
      const result = await response.json();
      throw new Error(
        Object.entries(result).map(([key, value]) => key + ":" + value + "!") +
          " " +
          response.status
      );
    }
  } catch (error) {
    throw new Error(error.message);
  }

  const response = await fetch(url, {
    method: method,
    headers: {
      authorization: `Bearer ${auth}`,
    },
    body: formData,
  });

  const result = await response.json();

  // if (!response.ok) {
  //     throw result;
  // }

  return result;
};

export const requestFactory = () => {
  return {
    get: requester.bind(null, "GET"),
    post: requester.bind(null, "POST"),
    put: requester.bind(null, "PUT"),
    patch: requester.bind(null, "PATCH"),
    delete: requester.bind(null, "DELETE"),
    upload: uploadTorrent.bind(null, "POST"),
  };
};

