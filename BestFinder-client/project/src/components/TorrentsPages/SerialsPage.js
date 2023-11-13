import { useEffect } from "react";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { MyNavBar } from "../Header/MyNavBar";
import style from "./Pages.module.css";
import { SerialArticle } from "./SerialArticle";
import { useForm } from "../useForm/useForm";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { useTranslation } from "react-i18next";

export const SerialPage = () => {

  const { t } = useTranslation();
  const { onSearchInTorrentValidator, valid } = useValidatorContext();
  const {
    onSortChange,
    setSelectorValue,
    onSortAndSearchLikes,
    torrentInfo,
    selectorValue,
    setTorrentInfo,
  } = useTorrentContext();

    useEffect(() => {
      if (valid) {
          onSortAndSearchLikes("serials", values.searchBar);
      }
    }, [valid]);
  
    useEffect(() => {
      if (Object.keys(torrentInfo).length === 0) {
        onSortChange("serials", "likes");
      }
    }, []);
  
    const onSort = (e) => {
      const value = e.target.value;
      setSelectorValue(value);
    };

    useEffect(() => {
      return () => {
          setTorrentInfo([]);
      };
    }, []);


      const { values, changeHandler } = useForm(
        {
          searchBar: "",
        });
     
        const onSubmit = async (e) => {
          e.preventDefault();
           await onSearchInTorrentValidator();
        }

    return(
        <>
        <MyNavBar />
        <section className={style.container}>
          <h2 className={style.header}>{t("categ.serials")}</h2>

          <div className={style.searchContainer}>
          <div className={style.formConainer}>
            <form className={style.formSearch} onSubmit={onSubmit}>
              <label className={style.lyrics} htmlFor="searchBar">
              {t("searchBar.search")}:
              </label>
              <input
                onChange={changeHandler}
                value={values.searchBar}
                type="text"
                className={style.inputSearchForm}
                id="searchBar"
                name="searchBar"
                placeholder="Search"
                autoComplete="on"
              />
              <button type="submit" className={style.btnSearchForm}>
              {t("searchBar.search")}
              </button>
            </form>
          </div>

          <div>
            <select
              value={selectorValue}
              className={style.selectCl}
              onChange={onSort}
            >
              <option className={style.opt} value="likes">
              {t("sort.likes")}
              </option>

            </select>
          </div>
        </div>

          {torrentInfo.map(serial => 
           <SerialArticle key={serial.id}
           torrent={serial.torrent}
          torrentUrl={serial.pictureUrl}
          torrentName={serial.serialName}
          seasons={serial.seasons}
          actors={serial.actors}
          categories={serial.categories}
          id={serial.id}
          />
          )
           }
        </section>
      </>
    )
}