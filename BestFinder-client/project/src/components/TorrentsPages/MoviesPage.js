import { MyNavBar } from "../Header/MyNavBar";
import style from "./Pages.module.css";
import { useEffect } from "react";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { MovieArticle } from "./MovieArticle";
import { useForm } from "../useForm/useForm";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { useTranslation } from "react-i18next";
import { Footer } from "../Footer/Footer";

export const MoviesPage = () => {
  const { t } = useTranslation();
  const { onSearchInTorrentValidator, valid } = useValidatorContext();
  const {
    onSortChange,
    setSelectorValue,
    onSortAndSearchLikes,
    onSortAndSearchYear,
    torrentInfo,
    moviesByYear,
    selectorValue,
    setTorrentInfo,
    setMoviesByYear,
  } = useTorrentContext();

  useEffect(() => {
    if (valid) {
      if (selectorValue === "likes") {
        onSortAndSearchLikes("movies", values.searchBar);
      } else if (selectorValue === "year") {
        onSortAndSearchYear("movies", values.searchBar);
      }
    }
  }, [valid, selectorValue]);

  useEffect(() => {
    if (selectorValue === "year" && Object.keys(moviesByYear).length === 0) {
      onSortChange("movies", "year");
    } else if (
      selectorValue === "likes" &&
      Object.keys(torrentInfo).length === 0
    ) {
      onSortChange("movies", "likes");
    }
  }, [selectorValue]);

  useEffect(() => {
    return () => {
        setTorrentInfo([]);
        setMoviesByYear([]);
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

  return (
    <>
      <MyNavBar />
      <section className={style.container}>
        <h2 className={style.header}>{t("categ.movies")}</h2>

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
        {selectorValue === "likes" &&
          torrentInfo.map((movie) => (
            <MovieArticle
              key={movie.id}
              torrent={movie.torrent}
              torrentUrl={movie.pictureUrl}
              torrentName={movie.movieName}
              year={movie.releasedYear}
              actors={movie.actors}
              categories={movie.categories}
              id={movie.id}
            />
          ))}
        {selectorValue === "year" &&
          moviesByYear.map((movie) => (
            <MovieArticle
              key={movie.id}
              torrent={movie.torrent}
              torrentUrl={movie.pictureUrl}
              torrentName={movie.movieName}
              year={movie.releasedYear}
              actors={movie.actors}
              categories={movie.categories}
              id={movie.id}
            />
          ))}
      </section>
      <Footer />

    </>
  );
};
