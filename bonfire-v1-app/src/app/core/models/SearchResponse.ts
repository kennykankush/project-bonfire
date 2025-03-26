export interface UserDTO {
    username: string;
    country: string;
    profilePicture: string;
  }

  export interface SearchResponse {
    users: UserDTO[];
  }