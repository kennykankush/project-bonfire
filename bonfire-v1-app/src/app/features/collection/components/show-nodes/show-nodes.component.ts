import { Component, inject, Input, OnInit } from '@angular/core';
import { NodeService } from '../../services/node.service';
import { Node } from '../../models/Bonfire';
import { Bonfire } from '../../models/Bonfire';


@Component({
  selector: 'app-show-nodes',
  standalone: false,
  templateUrl: './show-nodes.component.html',
  styleUrl: './show-nodes.component.css'
})
export class ShowNodesComponent implements OnInit {

  private nodeService = inject(NodeService);
  nodes!: Node
  private nodeMap = new Map<string, Node>();

  @Input()
  username!: string;

  selectedNodeId: string | null = null;
  nodeHistory: string[] = [];

  ngOnInit(): void {
    this.nodeService.getNodes(this.username).subscribe({
      next: (response) => {
        console.log(response);
        this.nodes = response;
        this.buildNodeMap(this.nodes);
        this.selectedNodeId = 'root';
        // console.log(this.nodeMap);
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
        console.log('Nodes has been fetched');
      }
    });

  }

  getCountries(): Node[] {
    if (!this.nodes || !this.nodes.child) {
      return [] 
    } 
    return Object.values(this.nodes.child)
      .filter(node => node.type === 'country');
  }

  private buildNodeMap(node: Node): void {
    if (!node) return;
    
    this.nodeMap.set(node.id, node);
    
    if (node.child) {
      Object.values(node.child).forEach(childNode => {
        this.buildNodeMap(childNode);
      });
    }
  }

  getNodeById(id: string | null): Node | null {
    if (!id) return null;
    return this.nodeMap.get(id) || null;
  }

  getChild(nodeId: string | null): Node[] {
    const node = this.getNodeById(nodeId);
    if (!node || !node.child) return [];
    return Object.values(node.child);
  }

  pathToNode(nodeId: string): Node[] {
    const path: Node[] = [];
    let currentNode = this.getNodeById(nodeId);

    while (currentNode) {
      path.unshift(currentNode); 
      
      let parentNode: Node | null = null;
      if (currentNode.id === 'root') break;
      
      for (const node of this.nodeMap.values()) {
        if (node.child && node.child[currentNode.id]) {
          parentNode = node;
          break;
        }
      }
      
      currentNode = parentNode;
    }
    
    return path;
  }

  nodeStats(nodeId: string): { 
    countries: number,
    regions: number,
    places: number,
    localities: number,
    bonfires: number 
  } {
    const node = this.getNodeById(nodeId);

    if (!node) return { 
      countries: 0, 
      regions: 0, 
      places: 0, 
      localities: 0, 
      bonfires: 0 
    };
    
    let countries = 0;
    let regions = 0;
    let places = 0;
    let localities = 0;
    let bonfires = 0;
    
    const countStats = (currentNode: Node) => {
      switch(currentNode.type) {
        case 'country':
          countries++;
          break;
        case 'region':
          regions++;
          break;
        case 'place':
          places++;
          break;
        case 'locality':
          localities++;
          break;
      }
      
      if (currentNode.bonfires) {
        bonfires += currentNode.bonfires.length;
      }
      
      if (currentNode.child) {
        Object.values(currentNode.child).forEach(childNode => {
          countStats(childNode);
        });
      }
    };
    
    countStats(node);
    
    return { countries, regions, places, localities, bonfires };
  }

  selectNode(nodeId: string): void {
    if (this.selectedNodeId) {
      this.nodeHistory.push(this.selectedNodeId);
    }
    
    this.selectedNodeId = nodeId;
    
    const selectedNode = this.getNodeById(nodeId);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  selectedNodeBonfire(): Bonfire[] {
    const node = this.getNodeById(this.selectedNodeId);
    if (!node || !node.bonfires) return [];
    
    return node.bonfires;
  }
  
  goBack(): void {
    if (this.nodeHistory.length > 0) {
      const previousNodeId = this.nodeHistory.pop();
      if (previousNodeId) {
        this.selectedNodeId = previousNodeId;
      }
    } else {
      this.selectedNodeId = 'root';
    }
  }


}