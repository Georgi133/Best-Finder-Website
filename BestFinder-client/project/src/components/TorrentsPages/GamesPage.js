import { MyNavBar } from "../Header/MyNavBar";
import style from "./Pages.module.css";
import { GameArticle } from "./GameArticle";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { useEffect } from "react";
import { SortingMenu } from "./SortingMenu";

export const GamesPage = () => {

    const { onPageMount, torrentInfo, selectorValue, gamesByYear } = useTorrentContext();
    useEffect(() => {
      async function fetchData() {
        return await onPageMount("game");
      }
      fetchData();
    },[]);

    return(
    <>
      <MyNavBar />
      <section className={style.container}>
        <h2 className={style.header}>Games</h2>

        <SortingMenu category="games" torrentInfoSortByYear={gamesByYear}/>


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