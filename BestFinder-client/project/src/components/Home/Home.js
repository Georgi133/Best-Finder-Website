import { MyNavBar } from "../Header/MyNavBar";
import { SectionList } from "../Main/SectionList";
import { Footer } from "../Footer/Footer";

export const Home = () => {
  return (
    <>
      <MyNavBar url={'home'}/>
      <SectionList />
      <Footer />
    </>
  );
};
