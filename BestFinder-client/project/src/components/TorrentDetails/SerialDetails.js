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

export const SerialDetails = () => {
  const { serialId } = useParams();
  const { userEmail } = useAuthContext();
  const { setValid,valid } = useValidatorContext();

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

    const onLike = (category) => {
      setLiked(!isLiked.liked);
      if(category === 'unlike') {
      onUnlikeTorrent({userEmail}, "serial", serialId);
      }else {
        onLikeTorrent({userEmail}, "serial", serialId);
      }
    
    }

    const token = JSON.parse(localStorage.getItem('token'));

    useEffect(() => {
      const decoded = jwt_decode(token);
      onTorrentDetails(serialId, "serial", userEmail === undefined ? decoded.sub : userEmail);
    },[]);

    const { values, changeHandler, onSubmit } = useForm({
      comment: '',
      category:'serial',
    }, onCommentSubmit);

    const onSubmitComment = (e) => {
      setValid(true);
      onSubmit(e);
    }

    const { t } = useTranslation();


  const videoUrl = torrentDetails.videoUrl;
  const serialName = torrentDetails.serialName;
  const categories = torrentDetails.categories;
  const seasons = torrentDetails.seasons;
  const resume = torrentDetails.resume;
  const pictureUrl = torrentDetails.pictureUrl;
  const actors = torrentDetails.actors;

  return (
    <>
      <MyNavBar />
     
      <section className={style.container}>
        <h3 className={style.header}>{serialName}</h3>
        <article className={style.innerContainer}>
          <div className={style.likes}>
            <p>{t("article.likes")}: {countLikes}</p>
          </div>
          
          <TorrentImage pictureUrl={pictureUrl}/>

          <div className={style.nameHolder}>
            <small className={style.sm}>{t("article.resume")}:</small>
            <strong className={style.movieName}>{resume}</strong>
          </div>
          <div className={style.cat}>
            <small className={style.sm}>{t("article.categories")}:</small>
            <strong className={style.categories}>{categories}</strong>
          </div>
          <div className={style.cat}>
            <small className={style.sm}>{t("article.actors")}:</small>
            <strong className={style.categories}>{actors}</strong>
          </div>
          <div className={style.releasedYear}>
            <small className={style.sm}>{t("article.seasons")}:</small> <strong>{seasons}</strong>
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
          <h4>{t("article.comments")}</h4>
          {comments.length === 0 && <p>{t("article.noComments")}</p>}
           { comments.map(c => {
           return (
           <Comment
            key={c.id} 
            torrentId={torrentDetails.id}
            commentId={c.id}
            torrent="serial"
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
