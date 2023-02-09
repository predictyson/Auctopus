export type IFilter = "main" | "like" | "category" | "startTime";

export interface IAuction {
  auctionSeq: number;
  email: string;
  title: string;
  startTime: string;
  likeCount: number;
  startPrice: number;
  state: number;
  auctionImage?: {
    auctionImageSeq: number;
    auctionSeq: number;
    imageUrl: string;
  };
}

export interface IReqSearch {
  word: string | null;
  category: string | null;
  state: number;
}

export interface IResLikeAuction {
  auctionSeq: number;
  userEmail: string;
  categorySeq: number;
  title: string;
  content: string;
  startTime: string;
  startPrice: number;
  bidUnit: number;
  likeCount: 29;
  state: 0;
}
