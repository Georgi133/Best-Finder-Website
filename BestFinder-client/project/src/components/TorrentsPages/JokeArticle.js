import { Link } from "react-router-dom";
import style from "./Pages.module.css";
import { useTranslation } from "react-i18next";

export const JokeArticle = ({
    torrent,
    torrentUrl,
    torrentName,
    shortText,
        id,
}) => {

  const { t } = useTranslation();

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
          <small className={style.sm}>{t("jokeAdd.name")}</small>{" "}
          <strong className={style.movieName}>{torrentName}</strong>
        </div>
        <div style={style.act}>
          <small className={style.sm}>{t("article.text")}:</small>{" "}
          <strong className={style.categories}>{shortText}</strong>
        </div>
        <div className={style.view}>
        <Link to={`${torrent.toLowerCase()}/${id}`}>{t("article.more")}</Link>
        </div>
      </article>
    </>
  );
};