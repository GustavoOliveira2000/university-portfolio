
// Importando Three.js
import * as THREE from 'three';
import { FontLoader } from 'three/addons/loaders/FontLoader.js';
import { TextGeometry } from 'three/addons/geometries/TextGeometry.js';

// Scene setup
const scene = new THREE.Scene();
const camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
const renderer = new THREE.WebGLRenderer({ antialias: true });
renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);

 // Set background color to white
 scene.background = new THREE.Color(0xffffff);

// Lighting
const ambientLight = new THREE.AmbientLight(0xffffff, 2);
scene.add(ambientLight);


  // Load font and create text
  const fontLoader = new FontLoader();
  fontLoader.load('https://threejs.org/examples/fonts/droid/droid_serif_regular.typeface.json', (font) => {
    const textGeometry = new TextGeometry('língua', {
      font: font,
      size: 1,
      height: 0.5,
      curveSegments: 1,
    });
  
    const material = new THREE.MeshStandardMaterial({ color: 0xff0000 });
    const textMesh = new THREE.Mesh(textGeometry, material);
    
    // Rotação inicial no eixo Z
    textMesh.rotation.y = THREE.MathUtils.degToRad(-5);

    // Adiciona o texto à cena
    textMesh.position.set(0, 0, 0);
    scene.add(textMesh);
  
    // Referência para o array de posições
    const positions = textGeometry.attributes.position.array;
  
    // Salva os valores iniciais dos vértices
    const initialPositions = positions.slice(); // Cria uma cópia do array original
  
    let animationStarted = false; // Flag para controlar o início da animação
    let offset = 0; // Controla o deslocamento da animação
    let pauseAnimation = false; // Flag para pausar a animação
  
    // Função de animação que altera os vértices
    const animateVertices = () => {
      if (!animationStarted || pauseAnimation) return; // Não anima se pausa ou atraso estiver ativo
  
      offset += 0.5; // Incremento para a animação
  
      let isAtInitialPosition = true;

      for (let i = 0; i < positions.length; i += 3) {
        positions[i] += Math.sin(offset + i * 0.01) * 0.05; // Move X com uma onda senoidal
        positions[i + 1] += Math.cos(offset + i * 0.01) * 0.05; // Move Y com uma onda cosenoidal
      
        // Verifica se algum vértice está fora da posição inicial
        if (Math.abs(positions[i] - initialPositions[i]) > 0.0001 || 
            Math.abs(positions[i + 1] - initialPositions[i + 1]) > 0.0001) {
          isAtInitialPosition = false;
        }
      }
      
      if (isAtInitialPosition) {
        pauseAnimation = true;
        setTimeout(() => (pauseAnimation = false), 3000);
      }
  
      // Marca os atributos como atualizados
      textGeometry.attributes.position.needsUpdate = true;
    };
  
    // Inicia a animação após 3 segundos
    setTimeout(() => {
      animationStarted = true; // Ativa a animação
    }, 3000);
  
    // Loop de animação
    const animate = () => {
      requestAnimationFrame(animate);
  
      // Atualiza os vértices
      animateVertices();
  
      // Renderiza a cena
      renderer.render(scene, camera);
    };
  
    animate();
  });

  // Camera setup
  camera.position.z = 5;

  // Handle resizing
  window.addEventListener('resize', () => {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
  });