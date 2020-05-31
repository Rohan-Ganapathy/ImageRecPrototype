package com.openImajDemo;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.BasicMatcher;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.image.processing.background.BasicBackgroundSubtract;
import org.openimaj.math.geometry.transforms.HomographyRefinement;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.geometry.transforms.estimation.RobustHomographyEstimator;
import org.openimaj.math.model.fit.RANSAC;

public class openimajdemo {

	public static void main(String[] args) throws MalformedURLException, IOException {
		// TODO Auto-generated method stub

		//MBFImage query = ImageUtilities.readMBF(new File("images/PopUp.jpg"));
		//MBFImage target = ImageUtilities.readMBF(new File("images/XButton.jpg"));

		MBFImage query = ImageUtilities.readMBF(new File("images/sombody.jpg"));
		MBFImage target = ImageUtilities.readMBF(new File("images/somface.jpg"));
		
		//MBFImage query = ImageUtilities.readMBF(new File("images/query.jpg"));
		//MBFImage target = ImageUtilities.readMBF(new File("images/target.jpg"));
		
		DoGSIFTEngine engine = new DoGSIFTEngine();
		LocalFeatureList<Keypoint> queryKeypoints = engine.findFeatures(query.flatten());
		LocalFeatureList<Keypoint> targetKeypoints = engine.findFeatures(target.flatten());
		
		LocalFeatureMatcher<Keypoint> matcher = new BasicMatcher<Keypoint>(80);

		RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(50.0, 1500,
				new RANSAC.PercentageInliersStoppingCondition(0.5));
		
		/*final RobustHomographyEstimator modelFitter = new
				RobustHomographyEstimator(5.0, 1500,
				new RANSAC.PercentageInliersStoppingCondition(0.5),
				HomographyRefinement.NONE);*/
		
		matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(new FastBasicKeypointMatcher<Keypoint>(5), modelFitter);

		matcher.setModelFeatures(queryKeypoints);
		matcher.findMatches(targetKeypoints);

		MBFImage consistentMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(), RGBColour.RED);

		DisplayUtilities.display(consistentMatches);
		
		System.out.println(consistentMatches.getBounds().transform(modelFitter.getModel().getTransform().inverse()).maxX());
		
		query.drawShape(
				target.getBounds().transform(modelFitter.getModel().getTransform()), 3,RGBColour.RED);
				DisplayUtilities.display(query);
				
				System.out.println(query.getBounds().transform(modelFitter.getModel().getTransform()).minX());
				System.out.println(query.getBounds().transform(modelFitter.getModel().getTransform()).minY());
				System.out.println(target.getBounds().transform(modelFitter.getModel().getTransform().inverse()).getHeight());
				System.out.println(target.getBounds().transform(modelFitter.getModel().getTransform().inverse()).getWidth());
				System.out.println(query.getBounds().transform(modelFitter.getModel().getTransform().inverse()).maxX());
				System.out.println(query.getBounds().transform(modelFitter.getModel().getTransform().inverse()).maxY());
				int xMin = (int) query.getBounds().transform(modelFitter.getModel().getTransform().inverse()).minX();
				int yMin = (int) query.getBounds().transform(modelFitter.getModel().getTransform().inverse()).minY();
				int xMax = (int) query.getBounds().transform(modelFitter.getModel().getTransform().inverse()).maxX();
				int yMax = (int) query.getBounds().transform(modelFitter.getModel().getTransform().inverse()).maxY();
				int h = (int) target.getBounds().transform(modelFitter.getModel().getTransform().inverse()).getHeight();
				int w = (int) target.getBounds().transform(modelFitter.getModel().getTransform().inverse()).getWidth();
				int height = (int) query.getBounds().getBottomRight().getY();
				int weight = (int) query.getBounds().getBottomRight().getX();
				
				
				System.out.println(w);
				System.out.println(h);
				
				System.out.println(target.getWidth());
				System.out.println(target.getHeight());
				
				//System.out.println(Math.abs(xMin)+w/2);
				//System.out.println(Math.abs(yMin)+h/2);
				
				//System.out.println(query.getBounds().transform(modelFitter.getModel().getTransform().inverse()).minimumBoundingRectangle());
				System.out.println(target.getBounds().transform(modelFitter.getModel().getTransform()).calculateCentroid().getX());
				System.out.println(target.getBounds().transform(modelFitter.getModel().getTransform()).calculateCentroid().getY());
				

	}

}
