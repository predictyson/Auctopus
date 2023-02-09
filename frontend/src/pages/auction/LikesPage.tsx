import React, { useEffect, useState } from "react";
import Layout from "@components/common/Layout";
import MainToggleButtonGroup from "@components/main/MainToggleButtonGroup";
import ItemsList from "@components/common/ItemsList";
import styled from "styled-components";
import ProfileImg from "@/assets/common/profile.png";
import { useNavigate } from "react-router-dom";
import { getAuctionLikes } from "@/api/auction";
import { IAuction } from "types/auction";
import { responseEncoding } from "axios";

const initLiveAuction: IAuction[] = [
  {
    auctionSeq: 1,
    email: "BIBI@naver.com",
    title: "내 찜이야",
    startTime: "2023-01-27 12:05",
    likeCount: 200,
    startPrice: 500000,
    state: 0,
    auctionImage: {
      auctionImageSeq: -1,
      auctionSeq: 0,
      imageUrl: "",
    },
  },
];

export default function LikesPage() {
  const navigate = useNavigate();
  const [live, setLive] = useState<"live" | "nonLive">("live");
  const [liveAuction, setLiveAuction] = useState<IAuction[]>(initLiveAuction);

  useEffect(() => {
    const fetchAuctionLikes = async () => {
      const res = await getAuctionLikes();
      console.log(res);
      if (res.status !== 200)
        throw new Error("내 찜 목록을 불러 올 수 없습니다 (❁´◡`❁)");

      const liveAuctionData: IAuction[] = [];
      for (const el of res.data) {
        liveAuctionData.push({
          auctionSeq: el.auctionSeq,
          email: el.userEmail,
          likeCount: el.likeCount,
          startPrice: el.startPrice,
          startTime: el.startTime,
          state: el.state,
          title: el.title,
          // FIXME: bid image url
          // auctionImage: ,
        });
      }
      setLiveAuction(liveAuctionData);
    };

    try {
      fetchAuctionLikes();
    } catch (error) {
      console.log(error);
      navigate("/error");
    }
  }, []);

  const changeLive = () => {
    setLive((prev) => (prev === "live" ? "nonLive" : "live"));
  };
  return (
    <Layout>
      <ProfileBox>
        <Profile src={ProfileImg} alt="profile" />
        <span className="profileTitle">정개미님의 관심목록</span>
      </ProfileBox>
      <MainToggleButtonGroup isMe live={live} onClick={changeLive} />
      <MarginBox />
      <ItemsList liveAuction={liveAuction} isLive={live === "live"} />
    </Layout>
  );
}

const ProfileBox = styled.div`
  flex: 0.2;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  .profileTitle {
    font-size: 1.8rem;
    margin: 1.2rem 0;
    font-family: Pretendard;
    text-align: center;
    font-weight: ${(props) => props.theme.fontWeight.semibold};
  }
`;

const Profile = styled.img`
  width: 6rem;
`;

const MarginBox = styled.div`
  height: 1.5rem;
`;
