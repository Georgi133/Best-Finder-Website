import { useEffect, useState } from "react";
import { MyNavBar } from "../Header/MyNavBar";
import style from "./AddMovie.module.css";
import { MenuFinder } from "./MenuFinder";
import { useTranslation } from "react-i18next";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";

export const AddMenu = () => {
  const [valueCategory, setValueCategory] = useState("category");
  const { addedMessage, setAddedMessage } = useTorrentContext();
  const { t } = useTranslation();
  const onChangeCategory = (e) => {
    const value = e.target.value;
    setValueCategory(value);
  };

  useEffect(() => {
    return () => {
      setAddedMessage(false);
    }
  },[])

  return (
    <>
      <MyNavBar />
      <div className={style.container}>
        <h2 className={style.header}>{t("addMenu.title")}</h2>
        <form>
          <select
            value={valueCategory}
            onChange={onChangeCategory}
            className={style.selectContainer}
          >
            <option hidden value="category">
            {t("addMenu.cat")}
            </option>
            <option className={style.opt} value="movie">
            {t("addMenu.movie")}
            </option>
            <option className={style.opt} value="serial">
            {t("addMenu.serial")}
            </option>
            <option className={style.opt} value="game">
            {t("addMenu.game")}
            </option>
            <option className={style.opt} value="anime">
            {t("addMenu.anime")}
            </option>
            <option className={style.opt} value="song">
            {t("addMenu.song")}
            </option>
            <option className={style.opt} value="joke">
            {t("addMenu.joke")}
            </option>
          </select>
        </form>
        {addedMessage && valueCategory === 'category' && <div className={style.success}>{t("successffully.success")}!</div>}
        {
          valueCategory && 
          <MenuFinder category={valueCategory} setValueCategory={setValueCategory}/>
        }

      </div>
    </>
  );
};
