import { Link } from "react-router-dom";
import style from "./Pages.module.css";
import { useTranslation } from "react-i18next";

export const SongArticle = ({
    torrent,
    torrentUrl,
    torrentName,
        year,
        singers,
        categories,
        id,
}) => {

  const { t } = useTranslation();

  return (
    <>
      <article className={style.innerContainer}>
        <div className={style.imgContainer}>
          <img
          alt="song"
            className={style.image}
            src={torrentUrl}
          />
        </div>
        <div className={style.nameHolder}>
          <small className={style.sm}>{t("songAdd.name")}</small>{" "}
          <strong className={style.movieName}>{torrentName}</strong>
        </div>
        <div style={style.act}>
          <small className={style.sm}>{t("article.categories")}:</small>{" "}
          <strong className={style.categories}>{categories}</strong>
        </div>
        {year && <div className={style.releasedYear}>
          <small className={style.sm}>{t("article.year")}:</small> <strong>{year}</strong>
        </div> }
 
        {singers && <div style={style.act}>
          <small className={style.sm}>{t("article.singers")}:</small>{" "}
          <strong className={style.nameActors}>{singers}</strong>
        </div>}
        <div className={style.view}>
        <Link to={`${torrent.toLowerCase()}/${id}`}>{t("article.more")}</Link>
        </div>
      </article>
    </>
  );
};