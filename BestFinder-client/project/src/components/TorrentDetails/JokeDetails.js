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
import { ErrorMessage } from "../ErrorMessage/ErrorMessage";

export const JokeDetails = () => {
  const { jokeId } = useParams();
  const { userEmail } = useAuthContext();
  const { validateComment } = useValidatorContext();

  const { onTorrentDetails, 
    torrentDetails, onCommentSubmit, 
    comments, onLikeTorrent, 
    onUnlikeTorrent , 
    isLiked ,
    setLiked, 
    countLikes,
    serverErrors,
    setServerErrors
   } =
    useTorrentContext();

    const onLike = (category) => {
      setLiked(!isLiked.liked);
      if(category === 'unlike') {
      onUnlikeTorrent("joke", jokeId);
      }else {
        onLikeTorrent("joke", jokeId);
      }
    
    }

    const { t } = useTranslation();

    const token = JSON.parse(localStorage.getItem('token'));

    useEffect(() => {
      const decoded = jwt_decode(token);
      onTorrentDetails(jokeId, "joke", userEmail === undefined ? decoded.sub : userEmail);
      window.scrollTo(0, 0);
    },[]);

    const { values, changeHandler, onSubmit, formErrors } = useForm({
      comment: '',
      category:'joke',
    }, onCommentSubmit);

    const onSubmitComment = (e) => {
      setServerErrors({});
      e.preventDefault();
      onSubmit(e,validateComment(values));
    }

  const jokeName = torrentDetails.jokeName;
  const pictureUrl = torrentDetails.pictureUrl;
  const text = torrentDetails.text;

  return (
    <>
      <MyNavBar />
     
      <section className={style.container}>
        <h3 className={style.header}>{jokeName}</h3>
        <article className={style.innerContainer}>
          <div className={style.likes}>
            <p>{t("article.likes")}: {countLikes}</p>
          </div>
          <TorrentImage pictureUrl={pictureUrl}/>

          <div className={style.cat}>
            <small className={style.sm}>{t("article.innerText")}:</small>
            <strong className={style.categories}>{text}</strong>
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

        <article className={style.commentContainer}>
          <h4>{t("article.comments")}</h4>
          {comments.length === 0 && <p>{t("article.noComments")}</p>}
           { comments.map(c => {
           return (
           <Comment
            key={c.id} 
            torrentId={torrentDetails.id}
            commentId={c.id}
            torrent="joke"
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
          <label htmlFor="comment">{t("article.writeComment")} </label>
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
