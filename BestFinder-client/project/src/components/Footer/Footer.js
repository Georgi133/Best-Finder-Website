import style from './Footer.module.css'
import { Link } from "react-router-dom";

export const Footer = () => {

    return(
        <footer className={style.footerCont}>
            <div className={style.social}>
              <Link to='https://www.facebook.com' className={style.socialLinks}><i className="fab fa-facebook"></i> Facebook</Link>
              <Link to='https://www.instagram.com' className={style.socialLinks}><i className="fab fa-instagram"></i> Instagram</Link>
              <Link to='https://twitter.com' className={style.socialLinks}><i className="fab fa-twitter"></i> Twitter</Link>
              <Link className={style.socialLinks} to='https://www.linkedin.com/in/georgi-stefanov-194918240'><i className="fab fa-linkedin"></i> Linked-In</Link>
           </div>

           <small className={style.smallEl}>&copy;  2023  by  Georgi Stefanov</small>
        </footer>
    );

}