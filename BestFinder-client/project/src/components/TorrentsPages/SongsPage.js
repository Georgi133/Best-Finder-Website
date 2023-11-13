import { useEffect } from "react";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { MyNavBar } from "../Header/MyNavBar";
import style from "./Pages.module.css";
import { SongArticle } from "./SongArticle";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { useForm } from "../useForm/useForm";
import { useTranslation } from "react-i18next";

export const SongsPage = () => {

  const { t } = useTranslation();
  const { onSearchInTorrentValidator, valid } = useValidatorContext();
  const {
    onSortChange,
    setSelectorValue,
    onSortAndSearchLikes,
    onSortAndSearchYear,
    torrentInfo,
    songsByYear,
    selectorValue,
    setTorrentInfo,
    setSongsByYear,

  } = useTorrentContext();

  useEffect(() => {
    if (valid) {
      if (selectorValue === "likes") {
        onSortAndSearchLikes("songs", values.searchBar);
      } else if (selectorValue === "year") {
        onSortAndSearchYear("songs", values.searchBar);
      }
    }
  }, [valid, selectorValue]);

  useEffect(() => {
    if (selectorValue === "year" && Object.keys(songsByYear).length === 0) {
      onSortChange("songs", "year");
    } else if (
      selectorValue === "likes" &&
      Object.keys(torrentInfo).length === 0
    ) {
      onSortChange("songs", "likes");
    }
  }, [selectorValue]);

  useEffect(() => {
    return () => {
        setTorrentInfo([]);
        setSongsByYear([]);
    };
  }, []);


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

    return(
        <>
        <MyNavBar />
        <section className={style.container}>
          <h2 className={style.header}>{t("categ.songs")}</h2>

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

              <option className={style.opt} value="year">
              {t("sort.year")}
              </option>
            </select>
          </div>
        </div>

          {selectorValue === 'likes' && torrentInfo.map(song => 
           <SongArticle key={song.id}
           torrent={song.torrent}
          torrentUrl={song.pictureUrl}
          torrentName={song.songName}
          year={song.releasedYear}
          singers={song.singers}
          categories={song.categories}
          id={song.id}
          />
          )
           }
            {selectorValue === 'year' && songsByYear.map(song => 
           <SongArticle key={song.id}
           torrent={song.torrent}
          torrentUrl={song.pictureUrl}
          torrentName={song.songName}
          year={song.releasedYear}
          singers={song.singers}
          categories={song.categories}
          id={song.id}
          />
          )
         }
        </section>
      </>
    )
}