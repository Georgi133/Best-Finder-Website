import { Link } from "react-router-dom";
import style from "./Pages.module.css";

export const SerialArticle  = ({
    torrent,
    torrentUrl,
    torrentName,
        seasons,
        actors,
        categories,
        id,
}) => {

  return (
    <>
      <article className={style.innerContainer}>
        <div className={style.imgContainer}>
          <img
          alt="serial"
            className={style.image}
            src={torrentUrl}
          />
        </div>
        <div className={style.nameHolder}>
          <small className={style.sm}>{torrent} name:</small>{" "}
          <strong className={style.movieName}>{torrentName}</strong>
        </div>
        <div style={style.act}>
          <small className={style.sm}>Categories:</small>{" "}
          <strong className={style.categories}>{categories}</strong>
        </div>
        {seasons && <div className={style.releasedYear}>
          <small className={style.sm}>Seasons:</small> <strong>{seasons}</strong>
        </div> }
        {actors && <div style={style.act}>
          <small className={style.sm}>Actors:</small>{" "}
          <strong className={style.nameActors}>{actors}</strong>
        </div>}
        <div className={style.view}>
        <Link to={`${torrent.toLowerCase()}/${id}`}>View More</Link>
        </div>
      </article>
    </>
  );
};
