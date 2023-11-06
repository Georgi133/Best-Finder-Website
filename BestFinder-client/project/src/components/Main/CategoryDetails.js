import { useEffect } from "react";
import style from "./SectionList.module.css";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";

export const CategoryDetails = ({
    imageUrl,
    setImageUrl,
    categoryName,
}) => {
    const { t } = useTranslation();
    const { onCategorySubPageMount, subCategoryPageInfo } = useTorrentContext()
    
    useEffect(() => {
        onCategorySubPageMount(categoryName.slice(0, categoryName.length - 1));
      },[]);

      const description = subCategoryPageInfo.description;
      const lastAddedOn = subCategoryPageInfo.lastAddedOn;

  return (
    <div className={style.overlay} >
    <div className={style.backdrop}></div>
    <div className={style.modal}>
        <div className={style.detailContainer}>
            <header className={style.headers}>
                <h2 className={style.headName}>{t(`categories.${categoryName}`)}</h2>
                <button className={style.btn} onClick={() => setImageUrl(null)} type="submit">X</button>
            </header>
            <div className={style.content}>
                <div className={style.imageContainer}>
                    <img className={style.image} src={imageUrl} alt='category-info'/>
                </div>
                <div className={style.userDetails}>
                    <p className={style.prg}>
                    <strong className={style.commonFields}>{t("description")}</strong>: {description}
                    </p>
                    <p className={style.prg}><strong className={style.commonFields}>{t("added")}</strong>: {lastAddedOn} </p>
                </div>
            </div>
            <Link className={style.movies} to={'/' + categoryName}>{t(`linked.${categoryName}`)}</Link>
        </div>
    </div>
</div >
  );
};
