import { MyNavBar } from "../Header/MyNavBar"
import style from './Server.module.css'

export const ServerError = () => {


    return (
      <div className={style.container}>
        <MyNavBar />
        <h1 className={style.head}>Server Error!</h1>
        <h2 className={style.head}>Excuse us!</h2>
        <h3 className={style.head}>Team of trained monkeys work over the problem!</h3>
      </div>
    )

}