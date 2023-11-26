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
import { ErrorMessage } from "../ErrorMessage/ErrorMessage";
import { useTranslation } from "react-i18next";
import { Footer } from "../Footer/Footer";

export const MovieDetails = () => {

  const { movieId } = useParams();
  const { userEmail } = useAuthContext();
  const { validateComment } = useValidatorContext();

  const { onTorrentDetails, 
    torrentDetails, onCommentSubmit, 
    comments, onLikeTorrent, 
    onUnlikeTorrent , 
    isLiked ,
    setLiked, 
    countLikes,
    prefixOfVideo,
    serverErrors,
    setServerErrors,
   } =
    useTorrentContext();


    const onLike = (category) => {
      setLiked(!isLiked.liked);
      if(category === 'unlike') {
      onUnlikeTorrent("movie", movieId);
      }else {
        onLikeTorrent("movie", movieId);
      }
    }

    const { t } = useTranslation();

    const token = JSON.parse(localStorage.getItem('token'));

    useEffect(() => {
      const decoded = jwt_decode(token);
      onTorrentDetails(movieId, "movie", userEmail === undefined ? decoded.sub : userEmail);
      window.scrollTo(0, 0);
    },[]);

    const { values, changeHandler, onSubmit, formErrors } = useForm({
      comment: '',
      category:'movie',
    }, onCommentSubmit);


    const onSubmitComment = (e) => {
      setServerErrors({});
      e.preventDefault();
      onSubmit(e,validateComment(values));
    }

  const videoUrl = torrentDetails.videoUrl;
  const movieName = torrentDetails.movieName;
  const categories = torrentDetails.categories;
  const year = torrentDetails.releasedYear;
  const resume = torrentDetails.resume;
  const pictureUrl = torrentDetails.pictureUrl;
  const actors = torrentDetails.actors;


  return (
    <>
      <MyNavBar />
     
      <section className={style.container}>
        <h3 className={style.header}>{movieName}</h3>
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
            <small className={style.sm}>{t("article.year")}:</small> <strong>{year}</strong>
          </div>
          {!isLiked.liked &&
          <button onClick={() => onLike('like')} className={style.likebtn} type="button">
            <i className="fas fa-thumbs-up"> {t("article.like")}</i>
          </button>}
          {isLiked.liked &&
          <button onClick={() => onLike('unlike')} className={style.liked} type="button">
          <i className="fas fa-thumbs-up"> {t("article.liked")}</i>
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
            torrent="movie"
            fullName={c.fullName}
             commentText={c.comment}
             emailOfUserOwnerOfTheComment={c.userEmail}
             />
             )
           })
           }
            
        </article>

     {formErrors.comment ? <ErrorMessage message={formErrors.comment}/> : 
     serverErrors.comment ? <ErrorMessage message={serverErrors.comment}/> : ''}
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
      <Footer />

    </>
  );
};
