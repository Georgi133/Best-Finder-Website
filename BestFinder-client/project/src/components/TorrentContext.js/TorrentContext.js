import { createContext, useContext, useState } from "react";
import { torrentServiceFactory } from "../services/TorrentService";
import { useNavigate } from "react-router-dom";
import { useAuthContext } from "../AuthContext/AuthContext";

export const TorrentContext = createContext();

export const TorrentProvider = ({ children }) => {
  const [torrentDetails, setTorrentDetails] = useState({});
  const [torrentInfo, setTorrentInfo] = useState([]);
  const [animesByYear, setAnimesByYear] = useState([]);
  const [moviesByYear, setMoviesByYear] = useState([]);
  const [songsByYear, setSongsByYear] = useState([]);
  const [gamesByYear, setGamesByYear] = useState([]);  
  const torrentService = torrentServiceFactory();
  const navigate = useNavigate();
  const [comments, setComments] = useState([]);
  const { userEmail } = useAuthContext();
  const [isLiked , setIsLiked] = useState({});
  const [countLikes, setCountLikes] = useState('');
  const [selectorValue , setSelectorValue] = useState('likes');
  const prefixOfVideo = "https://www.youtube.com/embed/";
  const [isTorrentAdded , setIsTorrentAdded] = useState(false);
  const [addedMessage, setAddedMessage] = useState(false);
  const [errorNumber , setErrorNumber] = useState(0);
  const [errorMessage , setErrorMessage] = useState('');
  const [serverErrors, setServerErrors] = useState({});
  const { onLogout } = useAuthContext();
  const [movieInfo, setMovieInfo] = useState({});
  const [serialInfo, setSerialInfo] = useState({});
  const [animeInfo, setAnimeInfo] = useState({});
  const [songInfo, setSongInfo] = useState({});
  const [jokeInfo, setJokeInfo] = useState({});
  const [gameInfo, setGameInfo] = useState({});
  const [torentDetailsInfo, setTorrentDetailsInfo] = useState([]);

  const [ isLangugeChanged , setIsLangugeChanged ] = useState(false);

  const onTorrentSubmit = async (data, torrent) => {
   
    try{
      const result = await torrentService.uploadTorrent(data, torrent);
    setIsTorrentAdded(result ? true : false);
    }catch (error) {
      ifServerThrowNavigate(error);
      setErrorNumber(convertErrorStringInNumber(error));
      const rawMessage = convertResponseMessage(error);
      messageOrFieldChecker(rawMessage);  
    }
    
  };

  const onSortAndSearchLikes =  async(category,data) => {
    const result = await torrentService.sortAndSearchByLikes(category, data ? {searchBar : data} : {searchBar : ' '});
    setTorrentInfo(result);
  }

  const onSortAndSearchYear = async (category,data) => {
    try{
      const result = await torrentService.sortAndSearchByYear(category, data ? {searchBar : data} : {searchBar : ' '});
      if(category === "movies") {
        setMoviesByYear(result);
      }else if(category === "animes"){
        setAnimesByYear(result);
      }else if(category === "songs"){
        setSongsByYear(result);
      }else if(category === "games"){
        setGamesByYear(result);
      }
    }catch (error) {
      ifServerThrowNavigate(error);
    }
   
  }

  const messageOrFieldChecker = (rawMessage) => {
    const index = rawMessage.indexOf(':');
      const nameOfField = rawMessage.substring(0, index); 
      if(nameOfField === 'message') {
        const message = rawMessage.substring(index + 1);
        setErrorMessage(message);
      }else {
        validFieldChecker(rawMessage);
      }
  }

  const validFieldChecker = (values) => {
    let arr = values.split(',');

    const errors = arr.reduce(function(acc, arr) {
      let newArray = arr.split(':');
      return{
        ...acc,
        [newArray[0]]:newArray[1],
      };
    }, {});
    
    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
     setServerErrors(errors);
  }

  const convertErrorStringInNumber = (errorString) => {
    return Number(errorString.message.substring(errorString.message.length - 4))
  }

  const convertResponseMessage = (message) => {
    return message.message.substring(0, message.message.length - 4);
  }

  const editLocalById = (torrentId, commentId, category, newValue) => {
    let isCommentChanged = false;

    const newComments = comments.map((comment) => {
      if (comment.id === commentId) {
        if (comment.comment !== newValue) {
          comment.comment = newValue;
          isCommentChanged = true;
        }
        return comment;
      }
      return comment;
    });
    setComments(newComments);
    if (isCommentChanged) {
      onEditComment(torrentId, commentId, category, newValue);
    }
  };

  const onDeleteComment = async (torrentId, commentId, category) => {
    try {
    const result = await torrentService.deleteComment(
      torrentId,
      commentId,
      category
    );
    setComments(result.comments);
    }catch (error) {
      ifServerThrowNavigate(error);
    }
  };


  const onEditComment = async (torrentId, commentId, category, data) => {
    try {
    const result = await torrentService.editCommenet(
      torrentId,
      commentId,
      category,
      { comment: data }
    );
    setComments(result.comments);
    }catch (error) {
      ifServerThrowNavigate(error);
    }
  };

  const onCommentSubmit = async (data) => {
    const { category, ...restData } = data;
    const newData = { ...restData, userEmail: userEmail };

    try {
      const result = await torrentService.uploadComment(
        newData,
        torrentDetails.id,
        category
      );
      setComments(result.comments);
    setTorrentDetails(result);
    } catch (error) {
      ifServerThrowNavigate(error);
      setErrorNumber(convertErrorStringInNumber(error));
      const rawMessage = convertResponseMessage(error);
      messageOrFieldChecker(rawMessage);  
    }
  };


  const onTorrentDetails = async (id, category, userEmail) => {

    const res = torentDetailsInfo.find(torrent => Number(torrent.id) === Number(id));
    if(res) {
      setCountLikes(res.countLikes);
      setComments(res.comments);
      setTorrentDetails(res);
      const isLiked = res.likes.find(like => like.userEmail === userEmail);
      setIsLiked({liked : isLiked ? isLiked.userEmail === userEmail : false});
    }else {
    try {
      const result = await torrentService.getById(id, category);
      setComments(result.comments);
      setIsLiked({liked :result.likedByUser});
      setTorrentDetails(result);
      setCountLikes(result.countLikes);
    }catch (error) {
      if(error.message.includes('not exist')) {
        navigate('/not-exist')
      }
      ifServerThrowNavigate(error);
    }
  }

  };

  const onLikeTorrent = async (category, id) => {
    try {
      const result = await torrentService.likeTorrent(category,id)
    setIsLiked({liked : result.likedByUser});
    console.log(result.countLikes);
    setCountLikes(result.countLikes);
    }catch (error) {
      ifServerThrowNavigate(error);
    }
    
  }

  const onUnlikeTorrent = async (category, id) => {
    try {
      const result = await torrentService.unLikeTorrent(category, id)
      setIsLiked({liked : result.likedByUser});
      console.log(result.countLikes);
      setCountLikes(result.countLikes);

    }catch (error) {
      ifServerThrowNavigate(error);
    }
   
  }

  const onPageMount = async (torrent) => {
    try {
      const result = await torrentService.getTorrent(torrent);
      setSelectorValue('likes')
      setTorrentInfo(result);
      return result;

    }catch (error) {
      ifServerThrowNavigate(error);
    }
   
  };

  const ifServerThrowNavigate = (error) => {
    if(error.message === 'forbidden' || error.message.includes('You are black listed')) {
      if(error.message.includes('You are black listed')){
        onLogout();
      }
      navigate('/not-allowed')
      return;
    }
    if(error.message === 'Failed to fetch') {
      navigate('/server-error');
    }
  }

  const setLiked = (isLiked) => {
    setIsLiked({liked : isLiked})
  }

  const onSortChange = async (category, criteria)  => {
    let result = '';
    if(criteria === 'year') {
      try{
        result = await torrentService.sortByYear(category);
        setTorrentDetailsInfo(result);

      }catch (error) {
        ifServerThrowNavigate(error);
      }
    }else if (criteria === 'likes') {
      try {
        result = await torrentService.getTorrent(category);
      setTorrentInfo(result);
      setTorrentDetailsInfo(result);
      return;

      }catch (error) {
        ifServerThrowNavigate(error);
      }
      
    }

    if(category === "movies" && criteria === 'year') {
      setMoviesByYear(result);
    }else if(category === "animes" && criteria === 'year'){
      setAnimesByYear(result);
    }else if(category === "songs" && criteria === 'year'){
      setSongsByYear(result);
    }else if(category === "games" && criteria === 'year'){
      setGamesByYear(result);
    }
    
  }


  const onCategorySubPageMount = async (category) => {
    try {
      const result = await torrentService.getCategoryInfo(category);
      if(category === 'movie') {
        setMovieInfo(result);
      } else if(category === 'song') {
        setSongInfo(result);
      } else if (category === 'joke') {
        setJokeInfo(result);
      } else if(category === 'anime') {
        setAnimeInfo(result);
      } else if (category === 'serial') {
        setSerialInfo(result);
      } else if (category === 'game') {
        setGameInfo(result);
      }
    }catch (error) {
      ifServerThrowNavigate(error);
      setErrorNumber(convertErrorStringInNumber(error));
      const rawMessage = convertResponseMessage(error);
      messageOrFieldChecker(rawMessage);  
    }
   
  }

  const torrentValues = {
    editLocalById,
    onCommentSubmit,
    onTorrentDetails,
    onTorrentSubmit,
    onPageMount,
    onDeleteComment,
    onLikeTorrent,
    onUnlikeTorrent,
    setLiked,
    onSortChange,
    setSelectorValue,
    onCategorySubPageMount,
    setIsTorrentAdded,
    setAddedMessage,
    setErrorMessage,
    setServerErrors,
    setIsLangugeChanged,
    onSortAndSearchLikes,
    onSortAndSearchYear,
    setTorrentInfo,
    setTorrentDetailsInfo,
    setMoviesByYear,
    setGamesByYear,
    setSongsByYear,
    setAnimesByYear,
    isLangugeChanged,
    movieInfo,
    serialInfo,
    animeInfo,
    jokeInfo,
    gameInfo,
    songInfo,
    errorMessage,
    serverErrors,
    addedMessage,
    isTorrentAdded,
    selectorValue,
    countLikes,
    isLiked,
    comments,
    torrentDetails,
    torrentInfo,
    prefixOfVideo,
    animesByYear,
    moviesByYear,
    songsByYear,
    gamesByYear,
  };

  return (
    <TorrentContext.Provider value={torrentValues}>
      {children}
    </TorrentContext.Provider>
  );
};

export const useTorrentContext = () => {
  const context = useContext(TorrentContext);

  return context;
};
