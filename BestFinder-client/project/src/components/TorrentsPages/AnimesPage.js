import { MyNavBar } from "../Header/MyNavBar";
import style from "./Pages.module.css";
import { AnimeArticle } from "./AnimeArticle";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { useEffect } from "react";
import { SortingMenu } from "./SortingMenu";

export const AnimesPage = () => {

    const { onPageMount, torrentInfo, animesByYear, selectorValue } = useTorrentContext();
    useEffect(() => {
      async function fetchData() {
        return await onPageMount("anime");
      }
      fetchData();
    },[]);
    
    return(
    <>
      <MyNavBar />
      <section className={style.container}>
        <h2 className={style.header}>Animes</h2>
  
    
       <SortingMenu category="animes" torrentInfoSortByYear={animesByYear}/>
        
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