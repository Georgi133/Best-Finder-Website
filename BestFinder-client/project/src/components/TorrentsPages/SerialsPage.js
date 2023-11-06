import { useEffect } from "react";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { MyNavBar } from "../Header/MyNavBar";
import style from "./Pages.module.css";
import { SerialArticle } from "./SerialArticle";
import { SortingMenu } from "./SortingMenu";

export const SerialPage = () => {

    const { onPageMount, torrentInfo ,serialsBySeasons, selectorValue} = useTorrentContext();

    useEffect(() => {
        async function fetchData() {
          return await onPageMount("serial");
        }
        fetchData();
      },[]);

    return(
        <>
        <MyNavBar />
        <section className={style.container}>
          <h2 className={style.header}>Serials</h2>

          <SortingMenu category="serials" torrentInfoSortByYear={serialsBySeasons} seasons='seasons'/>

          {selectorValue === 'likes' && torrentInfo.map(serial => 
           <SerialArticle key={serial.id}
           torrent={serial.torrent}
          torrentUrl={serial.pictureUrl}
          torrentName={serial.serialName}
          seasons={serial.seasons}
          actors={serial.actors}
          categories={serial.categories}
          id={serial.id}
          />
          )
           }
           {selectorValue === 'year' && serialsBySeasons.map(serial => 
         <SerialArticle key={serial.id}
         torrent={serial.torrent}
        torrentUrl={serial.pictureUrl}
        torrentName={serial.serialName}
        seasons={serial.seasons}
        actors={serial.actors}
        categories={serial.categories}
        id={serial.id}
        />
        )
         }
        </section>
      </>
    )
}