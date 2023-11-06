import { Link } from "react-router-dom";
import style from "./Pages.module.css";
import { useTranslation } from "react-i18next";

export const SerialArticle  = ({
    torrent,
    torrentUrl,
    torrentName,
        seasons,
        actors,
        categories,
        id,
}) => {

  const { t } = useTranslation();

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
          <small className={style.sm}>{t("serialAdd.name")}:</small>{" "}
          <strong className={style.movieName}>{torrentName}</strong>
        </div>
        <div style={style.act}>
          <small className={style.sm}>{t("article.categories")}:</small>{" "}
          <strong className={style.categories}>{categories}</strong>
        </div>
        {seasons && <div className={style.releasedYear}>
          <small className={style.sm}>{t("article.seasons")}:</small> <strong>{seasons}</strong>
        </div> }
        {actors && <div style={style.act}>
          <small className={style.sm}>{t("article.actors")}:</small>{" "}
          <strong className={style.nameActors}>{actors}</strong>
        </div>}
        <div className={style.view}>
        <Link to={`${torrent.toLowerCase()}/${id}`}>{t("article.more")}</Link>
        </div>
      </article>
    </>
  );
};
