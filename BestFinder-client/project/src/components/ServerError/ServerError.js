import { MyNavBar } from "../Header/MyNavBar"
import style from './Server.module.css'
import { Footer } from "../Footer/Footer";

export const ServerError = () => {


    return (
      <>
      <MyNavBar />
      <div className={style.container}>
        
        <h1 className={style.head}>Server Error!</h1>
        <h2 className={style.head}>Excuse us!</h2>
        <h3 className={style.head}>Team of trained monkeys work over the problem!</h3>
        <img className={style.picture} alt='errorPicture' src='https://media.istockphoto.com/id/174263946/photo/gorilla-businessman.jpg?s=612x612&w=0&k=20&c=PfK-Rc2syab0qLY8LkCc1mIEjO0uB41SzopH7i5AEVE='/>
      </div>
      <Footer />

      </>
      
    )

}