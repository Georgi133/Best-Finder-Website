import { useEffect } from "react";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { MyNavBar } from "../Header/MyNavBar";
import style from "./Pages.module.css";
import { SongArticle } from "./SongArticle";
import { SortingMenu } from "./SortingMenu";

export const SongsPage = () => {

    const { onPageMount, torrentInfo, selectorValue,songsByYear } = useTorrentContext();

    useEffect(() => {
        async function fetchData() {
          return await onPageMount("song");
        }
        fetchData();
      },[]);

    return(
        <>
        <MyNavBar />
        <section className={style.container}>
          <h2 className={style.header}>Songs</h2>

          <SortingMenu category="songs" torrentInfoSortByYear={songsByYear}/>

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