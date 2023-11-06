import { Link } from "react-router-dom";
import style from "./Pages.module.css";

export const MovieArticle = ({
    torrent,
    torrentUrl,
    torrentName,
        year,
        actors,
        categories,
        id,
}) => {


    return (
        <>
          <article className={style.innerContainer}>
            <div className={style.imgContainer}>
              <img
               alt="movie"
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
            {year && <div className={style.releasedYear}>
              <small className={style.sm}>Year:</small> <strong>{year}</strong>
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

}
