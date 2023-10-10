import { Footer } from "../Footer/Footer";
import { MyNavBar } from "../Header/MyNavBar";
import { SectionList } from "../Main/SectionList";

export const Home = () => {
  return (
    <>
      <MyNavBar url={'home'}/>
      <SectionList />
    </>
  );
};
