import { Link } from "react-router-dom";
import style from "./Pages.module.css";
import { useTranslation } from "react-i18next";

export const AnimeArticle = ({
    torrent,
    torrentUrl,
    torrentName,
        year,
        categories,
        id,
}) => {

  const { t } = useTranslation();

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
          <small className={style.sm}>{t("animeAdd.name")}:</small>{" "}
          <strong className={style.movieName}>{torrentName}</strong>
        </div>
        <div className={style.cat}>
          <small className={style.sm}>{t("article.categories")}:</small>{" "}
          <strong className={style.categories}>{categories}</strong>
        </div>
        <div className={style.releasedYear}>
          <small className={style.sm}>{t("article.year")}:</small> <strong>{year}</strong>
        </div> 
        <div className={style.view}>
        <Link to={`${torrent.toLowerCase()}/${id}`}>{t("article.more")}</Link>
        </div>
      </article>
    </>
  );
};