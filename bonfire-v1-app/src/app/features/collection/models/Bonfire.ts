export interface Bonfire {
  id: string;
  name: string;
  nodeId: string;
  maki: string;
  category: string;
  type: string;
  coordinates: [number, number];
}

export interface Node {
  id: string;
  name: string;
  type: string;
  coordinates: [number, number] | null;
  child: { [key: string]: Node };
  bonfires: Bonfire[] | null;
}