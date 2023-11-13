import { MyNavBar } from "../Header/MyNavBar";
import style from "./Pages.module.css";
import { GameArticle } from "./GameArticle";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { useEffect } from "react";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { useForm } from "../useForm/useForm";
import { useTranslation } from "react-i18next";

export const GamesPage = () => {

  const { t } = useTranslation();
  const { onSearchInTorrentValidator, valid } = useValidatorContext();
  const {
    onSortChange,
    setSelectorValue,
    onSortAndSearchLikes,
    onSortAndSearchYear,
    torrentInfo,
    gamesByYear,
    selectorValue,
    setTorrentInfo,
    setGamesByYear,
  
  } = useTorrentContext();

  useEffect(() => {
    if (valid) {
      if (selectorValue === "likes") {
        onSortAndSearchLikes("games", values.searchBar);
      } else if (selectorValue === "year") {
        onSortAndSearchYear("games", values.searchBar);
      }
    }
  }, [valid, selectorValue]);

  useEffect(() => {
    if (selectorValue === "year" && Object.keys(gamesByYear).length === 0) {
      onSortChange("games", "year");
    } else if (
      selectorValue === "likes" &&
      Object.keys(torrentInfo).length === 0
    ) {
      onSortChange("games", "likes");
    }
  }, [selectorValue]);

  const onSort = (e) => {
    const value = e.target.value;
    setSelectorValue(value);
  };


  const { values, changeHandler } = useForm({
    searchBar: "",
  });

  const onSubmit = async (e) => {
    e.preventDefault();
    await onSearchInTorrentValidator();
  };

  useEffect(() => {
    return () => {
        setTorrentInfo([]);
        setGamesByYear([])
    };
  }, []);

    return(
    <>
      <MyNavBar />
      <section className={style.container}>
        <h2 className={style.header}>{t("categ.games")}</h2>

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


        {selectorValue === 'likes' && torrentInfo.map(game => 
         <GameArticle key={game.id}
         torrent={game.torrent}
        torrentUrl={game.pictureUrl}
        torrentName={game.gameName}
        year={game.releasedYear}
        categories={game.categories}
        id={game.id}
        />
        )
         }
         {selectorValue === 'year' && gamesByYear.map(game => 
         <GameArticle key={game.id}
         torrent={game.torrent}
        torrentUrl={game.pictureUrl}
        torrentName={game.gameName}
        year={game.releasedYear}
        categories={game.categories}
        id={game.id}
        />
        )
         }

      </section>
    </>
    );
}