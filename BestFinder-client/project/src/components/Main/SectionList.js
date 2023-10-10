import { useState } from "react";
import { CategoryDetails } from "./CategoryDetails";
import style from "./SectionList.module.css";
import { useTranslation } from 'react-i18next';

const image = {
  game: "https://lumiere-a.akamaihd.net/v1/images/star-wars-outlaws-key-art-square_b893fc9e.jpeg?region=0%2C0%2C1080%2C1080",
  serials: "https://m.media-amazon.com/images/M/MV5BN2IzYzBiOTQtNGZmMi00NDI5LTgxMzMtN2EzZjA1NjhlOGMxXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_FMjpg_UX1000_.jpg",
  movie: "https://flxt.tmsimg.com/assets/p34442_p_v10_aq.jpg",
  anime: "https://c4.wallpaperflare.com/wallpaper/223/136/926/anime-naruto-itachi-uchiha-kunai-naruto-wallpaper-preview.jpg",
  song: "https://i.dailymail.co.uk/i/pix/2015/10/30/15/2DEE63CA00000578-0-image-m-56_1446220555799.jpg",
  joke: "https://ca-times.brightspotcdn.com/dims4/default/f101902/2147483647/strip/true/crop/319x425+0+0/resize/319x425!/quality/75/?url=https%3A%2F%2Fcalifornia-times-brightspot.s3.amazonaws.com%2F7a%2F6d%2F26848a33b339c56341157969f1f8%2Flat-carrey10-fvg634ke20071128160647",
}
const category = {
  movie: "movie",
  joke: "joke",
  anime: "anime",
  song: "song",
  game: "game",
  serial: "serial",
}

export const SectionList = () => {
  const [imageUrl, setImageUrl] = useState(null);
  const [categoryName, setCategoryName] = useState('');
  const { t } = useTranslation();

  const onClickArticle = (category) => {
    
    if(category === 'movie') {
      setImageUrl(image.movie);
    } else if(category === 'song') {
      setImageUrl(image.song);
    } else if (category === 'joke') {
      setImageUrl(image.joke);
    } else if(category === 'anime') {
      setImageUrl(image.anime);
    } else if (category === 'serial') {
      setImageUrl(image.serials);
    } else if (category === 'game') {
      setImageUrl(image.game);
    }

    const categoryCopy = category[0] + category.slice(1) + 's';
    setCategoryName(categoryCopy);
  }

  return (
    <>
      {imageUrl && <CategoryDetails imageUrl={imageUrl} categoryName={categoryName} setImageUrl={setImageUrl}/>}
      <section className={style.container}>
        <h1 className={style.header}>{t("header")}</h1>

        <div className={style.group1}>
          <article onClick={() => onClickArticle(category.movie)} className={style.movie}>
            <header>{t("movies")}</header>
          </article>
          <article  onClick={() => onClickArticle(category.serial)} className={style.series}>
            <header>{t("serials")}</header>
          </article>
        </div>

        <div className={style.group2}>
          <article  onClick={() => onClickArticle(category.game)} className={style.game}>
            <header>{t("games")}</header>
          </article>
          <article  onClick={() => onClickArticle(category.song)} className={style.song}>
            <header>{t("songs")}</header>
          </article>
        </div>

        <div className={style.group3}>
          <article  onClick={() => onClickArticle(category.anime)} className={style.anime}>
            <header>{t("animes")}</header>
          </article>
          <article  onClick={() => onClickArticle(category.joke)} className={style.joke}>
            <header>{t("jokes")}</header>
          </article>
        </div>
      </section>
    </>
  );
};
