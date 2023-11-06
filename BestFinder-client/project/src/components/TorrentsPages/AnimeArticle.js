import { Link } from "react-router-dom";
import style from "./Pages.module.css";

export const AnimeArticle = ({
    torrent,
    torrentUrl,
    torrentName,
        year,
        categories,
        id,
}) => {

  return (
    <>
      <article className={style.innerContainer}>
        <div className={style.imgContainer}>
          <img
          alt="anime"
            className={style.image}
            src={torrentUrl}
          />
        </div>
        <div className={style.nameHolder}>
          <small className={style.sm}>{torrent} name:</small>{" "}
          <strong className={style.movieName}>{torrentName}</strong>
        </div>
        <div className={style.cat}>
          <small className={style.sm}>Categories:</small>{" "}
          <strong className={style.categories}>{categories}</strong>
        </div>
        <div className={style.releasedYear}>
          <small className={style.sm}>Year:</small> <strong>{year}</strong>
        </div> 
        <div className={style.view}>
        <Link to={`${torrent.toLowerCase()}/${id}`}>View More</Link>
        </div>
      </article>
    </>
  );
};