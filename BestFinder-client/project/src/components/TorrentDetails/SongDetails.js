import { useParams } from "react-router-dom";
import style from "./Details.module.css";
import { MyNavBar } from "../Header/MyNavBar";
import { ButtonSubmit } from "../Login/ButtonSubmit";
import { useEffect } from "react";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { Comment } from "./Comment";
import { useAuthContext } from "../AuthContext/AuthContext";
import  jwt_decode  from 'jwt-decode'
import { useForm } from "../useForm/useForm";
import { TorrentImage } from "./TorrentImage";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { useTranslation } from "react-i18next";

export const SongDetails = () => {
  const { songId } = useParams();
  const { userEmail } = useAuthContext();
  const { setValid } = useValidatorContext();

  const { onTorrentDetails, 
    torrentDetails, onCommentSubmit, 
    comments, onLikeTorrent, 
    onUnlikeTorrent , 
    isLiked ,
    setLiked, 
    countLikes,
    prefixOfVideo,
   } =
    useTorrentContext();
    const { t } = useTranslation();

    const onLike = (category) => {
      setLiked(!isLiked.liked);
      if(category === 'unlike') {
      onUnlikeTorrent({userEmail}, "song", songId);
      }else {
        onLikeTorrent({userEmail}, "song", songId);
      }
    
    }

    const token = JSON.parse(localStorage.getItem('token'));

    useEffect(() => {
      const decoded = jwt_decode(token);
      onTorrentDetails(songId, "song", userEmail === undefined ? decoded.sub : userEmail);
    },[]);

    const { values, changeHandler, onSubmit } = useForm({
      comment: '',
      category:'song',
    }, onCommentSubmit);

    const onSubmitComment = (e) => {
      setValid(true);
      onSubmit(e);
    }


  const videoUrl = torrentDetails.videoUrl;
  const songName = torrentDetails.songName;
  const categories = torrentDetails.categories;
  const year = torrentDetails.releasedYear;
  const pictureUrl = torrentDetails.pictureUrl;
  const singers = torrentDetails.singers;

  return (
    <>
      <MyNavBar />
     
      <section className={style.container}>
        <h3 className={style.header}>{songName}</h3>
        <article className={style.innerContainer}>
          <div className={style.likes}>
            <p>{t("article.likes")}: {countLikes}</p>
          </div>
        
          <TorrentImage pictureUrl={pictureUrl}/>

          <div className={style.cat}>
            <small className={style.sm}>{t("article.categories")}:</small>
            <strong className={style.categories}>{categories}</strong>
          </div>
          <div className={style.cat}>
            <small className={style.sm}>{t("article.singers")}:</small>
            <strong className={style.categories}>{singers}</strong>
          </div>
          <div className={style.releasedYear}>
            <small className={style.sm}>{t("article.year")}::</small> <strong>{year}</strong>
          </div>
          {!isLiked.liked &&
          <button onClick={() => onLike('like')} className={style.likebtn} type="button">
            <i className="fas fa-thumbs-up"> {t("article.like")}:</i>
          </button>}
          {isLiked.liked &&
          <button onClick={() => onLike('unlike')} className={style.liked} type="button">
          <i className="fas fa-thumbs-up"> {t("article.liked")}:</i>
        </button>}

        </article>

        <article className={style.videoContainer}>
          <iframe
            className={style.frame}
            src={`${prefixOfVideo}${videoUrl}`}
            title="YouTube video"
            allowFullScreen="accelerometer; 
                autoplay; 
                clipboard-write; 
                encrypted-media; 
                gyroscope; 
                picture-in-picture; 
                web-share"
          ></iframe>
        </article>

        <article className={style.commentContainer}>
          <h4>{t("article.comments")}:</h4>
          {comments.length === 0 && <p>{t("article.noComments")}:</p>}
           { comments.map(c => {
           return (
           <Comment
            key={c.id} 
            torrentId={torrentDetails.id}
            commentId={c.id}
            torrent="song"
            fullName={c.fullName}
             commentText={c.comment}
             emailOfUserOwnerOfTheComment={c.userEmail}
             />
             )
           })
              
           }
            
        </article>
        <form className={style.formContainer} onSubmit={onSubmitComment}>
          <label htmlFor="comment">{t("article.writeComment")}: </label>
          <textarea
            className={style.textArea}
            value={values.comment}
            onChange={changeHandler}
            id="comment"
            name="comment"
            type="text"
          ></textarea>
          <ButtonSubmit />
        </form>
      </section>
    </>
  );
};
