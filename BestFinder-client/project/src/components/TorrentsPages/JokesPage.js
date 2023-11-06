import { MyNavBar } from "../Header/MyNavBar";
import style from "./Pages.module.css";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { useEffect } from "react";
import { JokeArticle } from "./JokeArticle";

export const JokesPage = () => {

    const { onPageMount, torrentInfo } = useTorrentContext();
    useEffect(() => {
      async function fetchData() {
        return await onPageMount("joke");
      }
      fetchData();
    },[]);

    return(
    <>
      <MyNavBar />
      <section className={style.container}>
        <h2 className={style.header}>Jokes</h2>
        <select
        value="likes"
        className={style.selectCl}
        >
        <option 
        className={style.opt}
         value="likes"
         hidden
         >
        Sorted by likes
        </option>
        </select>
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