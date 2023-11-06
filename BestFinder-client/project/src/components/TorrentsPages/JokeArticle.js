import { Link } from "react-router-dom";
import style from "./Pages.module.css";

export const JokeArticle = ({
    torrent,
    torrentUrl,
    torrentName,
    shortText,
        id,
}) => {

  return (
    <>
      <article className={style.innerContainer}>
        <div className={style.imgContainer}>
          <img
          alt="game"
            className={style.image}
            src={torrentUrl}
          />
        </div>
        <div className={style.nameHolder}>
          <small className={style.sm}>{torrent} name:</small>{" "}
          <strong className={style.movieName}>{torrentName}</strong>
        </div>
        <div style={style.act}>
          <small className={style.sm}>Short text:</small>{" "}
          <strong className={style.categories}>{shortText}</strong>
        </div>
        <div className={style.view}>
        <Link to={`${torrent.toLowerCase()}/${id}`}>View More</Link>
        </div>
      </article>
    </>
  );
};