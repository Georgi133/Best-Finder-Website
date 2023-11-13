import { MyNavBar } from "../Header/MyNavBar";
import style from "./Pages.module.css";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { useEffect } from "react";
import { JokeArticle } from "./JokeArticle";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { useForm } from "../useForm/useForm";
import { useTranslation } from "react-i18next";

export const JokesPage = () => {

  const { t } = useTranslation();
  const { onSearchInTorrentValidator, valid } = useValidatorContext();
  const {
    onSortChange,
    setSelectorValue,
    onSortAndSearchLikes,
    selectorValue,
    torrentInfo,
    setTorrentInfo,
  } = useTorrentContext();

  useEffect(() => {
    if (valid) {
        onSortAndSearchLikes("jokes", values.searchBar);
    }
  }, [valid]);

  useEffect(() => {
    if (Object.keys(torrentInfo).length === 0) {
      onSortChange("jokes", "likes");
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
        <h2 className={style.header}>{t("categ.jokes")}</h2>
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

          <div className={style.sortContainer}>
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
        {torrentInfo.map(joke => 
         <JokeArticle key={joke.id}
         torrent={joke.torrent}
        torrentUrl={joke.pictureUrl}
        torrentName={joke.jokeName}
        shortText={joke.shortText}
        id={joke.id}
        />
        )
         }
      </section>
    </>
    );
}