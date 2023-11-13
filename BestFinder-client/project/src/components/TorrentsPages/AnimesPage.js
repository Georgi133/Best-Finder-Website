import { MyNavBar } from "../Header/MyNavBar";
import style from "./Pages.module.css";
import { AnimeArticle } from "./AnimeArticle";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { useEffect } from "react";
import { useForm } from "../useForm/useForm";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { useTranslation } from "react-i18next";

export const AnimesPage = () => {
  const { t } = useTranslation();

  const { onSearchInTorrentValidator, valid } = useValidatorContext();
  const {
    onSortChange,
    setSelectorValue,
    onSortAndSearchLikes,
    onSortAndSearchYear,
    torrentInfo,
    animesByYear,
    selectorValue,
    setTorrentInfo,
    setAnimesByYear,
  } = useTorrentContext();

  useEffect(() => {
    if (valid) {
      if (selectorValue === "likes") {
        onSortAndSearchLikes("animes", values.searchBar);
      } else if (selectorValue === "year") {
        onSortAndSearchYear("animes", values.searchBar);
      }
    }
  }, [valid, selectorValue]);

  useEffect(() => {
    if (selectorValue === "year" && Object.keys(animesByYear).length === 0) {
      onSortChange("animes", "year");
    } else if (
      selectorValue === "likes" &&
      Object.keys(torrentInfo).length === 0
    ) {
      onSortChange("animes", "likes");
    }
  }, [selectorValue]);

  const onSort = (e) => {
    const value = e.target.value;
    setSelectorValue(value);
  };


  const { values, changeHandler } = useForm({
    searchBar: "",
  });

  useEffect(() => {
    return () => {
        setTorrentInfo([]);
        setAnimesByYear([]);
    };
  }, []);

  const onSubmit = async (e) => {
    e.preventDefault();
    await onSearchInTorrentValidator();
  };
    
    return(
    <>
      <MyNavBar />
      <section className={style.container}>
        <h2 className={style.header}>{t("categ.animes")}</h2>
  
    
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

              <option className={style.opt} value="year">
              {t("sort.year")}
              </option>
            </select>
          </div>
        </div>
        
        {selectorValue === 'likes' && torrentInfo.map(anime => 
         <AnimeArticle key={anime.id}
         torrent={anime.torrent}
        torrentUrl={anime.pictureUrl}
        torrentName={anime.animeName}
        year={anime.releasedYear}
        categories={anime.categories}
        id={anime.id}
        />
        )
         }
         {selectorValue === 'year' && animesByYear.map(anime => 
         <AnimeArticle key={anime.id}
         torrent={anime.torrent}
        torrentUrl={anime.pictureUrl}
        torrentName={anime.animeName}
        year={anime.releasedYear}
        categories={anime.categories}
        id={anime.id}
        />
        )
         }
      </section>
    </>
    );
}