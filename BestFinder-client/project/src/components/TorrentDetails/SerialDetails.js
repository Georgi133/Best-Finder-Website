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
            <p>Likes: {countLikes}</p>
          </div>
          
          <TorrentImage pictureUrl={pictureUrl}/>

          <div className={style.nameHolder}>
            <small className={style.sm}>Resume:</small>
            <strong className={style.movieName}>{resume}</strong>
          </div>
          <div className={style.cat}>
            <small className={style.sm}>Categories:</small>
            <strong className={style.categories}>{categories}</strong>
          </div>
          <div className={style.cat}>
            <small className={style.sm}>Actors:</small>
            <strong className={style.categories}>{actors}</strong>
          </div>
          <div className={style.releasedYear}>
            <small className={style.sm}>Seasons:</small> <strong>{seasons}</strong>
          </div>
          {!isLiked.liked &&
          <button onClick={() => onLike('like')} className={style.likebtn} type="button">
            <i className="fas fa-thumbs-up"> Like</i>
          </button>}
          {isLiked.liked &&
          <button onClick={() => onLike('unlike')} className={style.liked} type="button">
          <i className="fas fa-thumbs-up"> Liked</i>
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
          <h4>Comments</h4>
          {comments.length === 0 && <p>No Comments</p>}
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
          <label htmlFor="comment">Write Comment: </label>
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
